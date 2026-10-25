package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimmanagement.*;
import org.example.insurance_policy_system.entity.claimmanagement.*;
import org.example.insurance_policy_system.enums.claimmanagement.SettlementStatus;
import org.example.insurance_policy_system.repository.claimmanagement.*;
import org.example.insurance_policy_system.service.claimmanagement.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClaimSettlementServiceImpl implements ClaimSettlementService {

    private final ClaimMasterRepository claims;
    private final ClaimAssessmentRepository assessments;
    private final ClaimBeneficiaryRepository beneficiaries;
    private final ClaimSettlementRepository settlements;
    private final RazorpayXService razorpay;
    private final ClaimHistoryService history;

    // Demo: replace with authenticated employee ID.
    private static final Long DEMO_EMPLOYEE_ID = 1L;

    @Override
    @Transactional
    public ClaimSettlementResponseDto processSettlement(
            Long claimId,
            ClaimSettlementRequestDto request) {

        ClaimMaster claim = claims.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Claim not found"));

        // 1. Only approved claims
        if (!"APPROVED".equalsIgnoreCase(
                String.valueOf(claim.getClaimStatus()))) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Claim must be approved before settlement");
        }

        // 2. Duplicate check
        if (settlements.findByClaimId_ClaimId(claimId).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Settlement already exists");
        }

        // 3. Amount from backend assessment
        // Assumes findByClaim_ClaimId returns a List.
        ClaimAssessment assessment = assessments
                .findByClaim_ClaimId(claimId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Claim assessment not found"));

        BigDecimal netPayable = assessment.getNetPayable();

        if (netPayable == null || netPayable.signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid net payable amount");
        }

        // 4. Beneficiary verification
        ClaimBeneficiary beneficiary = beneficiaries
                .findByClaimId(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Bank details missing"));

        if (!"DEMO_VERIFIED".equals(
                beneficiary.getVerificationStatus())
                || beneficiary.getRazorpayFundAccountId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Bank demo verification required");
        }

        // 5. Payment mode
        String paymentMode = request.getPaymentMode()
                .trim()
                .toUpperCase(Locale.ROOT);

        if (!Set.of("IMPS", "NEFT", "RTGS").contains(paymentMode)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Supported bank modes: IMPS, NEFT, RTGS");
        }

        // 6. Save settlement
        ClaimSettlement settlement = new ClaimSettlement();

        settlement.setClaimId(claim);
        settlement.setApprovedAmount(netPayable);
        settlement.setPaymentMode(paymentMode);
        settlement.setSettlementStatus(SettlementStatus.PENDING);
        settlement.setIdempotencyKey(UUID.randomUUID().toString());

        settlement = settlements.save(settlement);

        history.recordHistory(
                claimId,
                "SETTLEMENT_CREATED",
                DEMO_EMPLOYEE_ID,
                "Net payable: " + netPayable
        );

        return toDto(settlement);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimSettlementResponseDto getSettlement(Long claimId) {

        ClaimSettlement settlement = settlements
                .findByClaimId_ClaimId(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Settlement not found"));

        return toDto(settlement);
    }

    // Initiates RazorpayX test payout.
    // Synchronized is only an in-process demo guard.
    public synchronized PayoutStatusResponseDto disburse(Long settlementId) {

        ClaimSettlement settlement = findSettlement(settlementId);

        if (settlement.getSettlementStatus() != SettlementStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Settlement is not pending");
        }

        // If payout exists, check status instead of creating another.
        if (settlement.getRazorpayPayoutId() != null) {
            return checkPayoutStatus(settlementId);
        }

        Long claimId = settlement.getClaimId().getClaimId();

        ClaimBeneficiary beneficiary = beneficiaries
                .findByClaimId(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Beneficiary not found"));

        if (!"DEMO_VERIFIED".equals(
                beneficiary.getVerificationStatus())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Bank not demo verified");
        }

        // Amount comes from persisted netPayable, not Angular.
        JsonNode payout = razorpay.createPayout(
                beneficiary.getRazorpayFundAccountId(),
                settlement.getApprovedAmount(),
                settlement.getPaymentMode(),
                settlement.getIdempotencyKey()
        );

        String payoutId = payout.path("id").asText("");

        if (payoutId.isBlank()) {
            throw new IllegalStateException(
                    "RazorpayX did not return payout ID");
        }

        settlement.setRazorpayPayoutId(payoutId);
        settlements.save(settlement);

        return reconcile(settlement, payout);
    }

    public PayoutStatusResponseDto checkPayoutStatus(Long settlementId) {

        ClaimSettlement settlement = findSettlement(settlementId);

        if (settlement.getRazorpayPayoutId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Payout not initiated");
        }

        JsonNode payout = razorpay.getPayout(
                settlement.getRazorpayPayoutId());

        return reconcile(settlement, payout);
    }

    @Transactional
    public synchronized PayoutStatusResponseDto reconcile(
            ClaimSettlement settlement,
            JsonNode payout) {

        String providerStatus = payout.path("status")
                .asText("unknown")
                .toLowerCase(Locale.ROOT);

        if ("processed".equals(providerStatus)
                && settlement.getSettlementStatus() != SettlementStatus.PAID) {

            settlement.setSettlementStatus(SettlementStatus.PAID);
            settlement.setSettlementDate(LocalDateTime.now());
            settlement.setTransactionReference(
                    settlement.getRazorpayPayoutId());

            settlement.setSettlementAdviceNumber(
                    "ADV-" + settlement.getSettlementId());

            ClaimMaster claim = settlement.getClaimId();

            // Adjust enum name only if your ClaimStatus differs.
            claim.setClaimStatus(
                    org.example.insurance_policy_system.enums
                            .claimmanagement.ClaimStatus.SETTLED
            );

            claims.save(claim);

            history.recordHistory(
                    claim.getClaimId(),
                    "SETTLEMENT_COMPLETED",
                    DEMO_EMPLOYEE_ID,
                    "Payout: " + settlement.getRazorpayPayoutId()
            );

        } else if (Set.of(
                "failed", "rejected", "cancelled", "reversed"
        ).contains(providerStatus)
                && settlement.getSettlementStatus() != SettlementStatus.PAID
                && settlement.getSettlementStatus() != SettlementStatus.FAILED) {

            settlement.setSettlementStatus(SettlementStatus.FAILED);

            history.recordHistory(
                    settlement.getClaimId().getClaimId(),
                    "SETTLEMENT_FAILED",
                    DEMO_EMPLOYEE_ID,
                    "Provider status: " + providerStatus
            );
        }

        settlements.save(settlement);

        return new PayoutStatusResponseDto(
                settlement.getSettlementId(),
                settlement.getClaimId().getClaimId(),
                settlement.getApprovedAmount(),
                settlement.getPaymentMode(),
                settlement.getSettlementStatus().name(),
                providerStatus,
                settlement.getRazorpayPayoutId(),
                settlement.getTransactionReference(),
                settlement.getSettlementAdviceNumber()
        );
    }

    private ClaimSettlement findSettlement(Long settlementId) {

        return settlements.findById(settlementId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Settlement not found"));
    }

    private ClaimSettlementResponseDto toDto(ClaimSettlement settlement) {

        return new ClaimSettlementResponseDto(
                settlement.getSettlementId(),
                settlement.getClaimId().getClaimId(),
                settlement.getApprovedAmount(),
                settlement.getPaymentMode(),
                settlement.getTransactionReference(),
                settlement.getSettlementDate(),
                settlement.getSettlementStatus()
        );
    }

    public ClaimSettlement getPaidForAdvice(Long settlementId) {

        ClaimSettlement settlement = findSettlement(settlementId);

        if (settlement.getSettlementStatus() != SettlementStatus.PAID) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Advice available only after successful payout");
        }

        return settlement;
    }
}