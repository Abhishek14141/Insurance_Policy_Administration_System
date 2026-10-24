package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimmanagement.*;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimBeneficiary;
import org.example.insurance_policy_system.repository.claimmanagement.*;
import org.example.insurance_policy_system.service.claimmanagement.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BankVerificationServiceImpl implements BankVerificationService {

    private final ClaimBeneficiaryRepository beneficiaries;
    private final ClaimMasterRepository claims;
    private final RazorpayXService razorpay;

    @Override
    public BankDetailsResponseDto save(
            Long claimId,
            BankDetailsRequestDto req) {

        if (!claims.existsById(claimId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Claim not found");
        }

        if (!req.getAccountNumber()
                .equals(req.getConfirmAccountNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Account numbers do not match");
        }

        if (beneficiaries.findByClaimId(claimId).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Bank details already saved for this claim");
        }

        ClaimBeneficiary beneficiary = new ClaimBeneficiary();

        beneficiary.setClaimId(claimId);
        beneficiary.setAccountHolderName(req.getAccountHolderName());
        beneficiary.setAccountNumber(req.getAccountNumber());
        beneficiary.setIfscCode(
                req.getIfscCode().toUpperCase(Locale.ROOT));
        beneficiary.setBankName(req.getBankName());

        return toResponse(beneficiaries.save(beneficiary));
    }

    @Override
    public BankDetailsResponseDto demoVerify(Long claimId) {

        ClaimBeneficiary beneficiary = beneficiaries
                .findByClaimId(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Beneficiary not found"));

        if (beneficiary.getRazorpayContactId() == null) {

            String contactId = razorpay.createContact(
                    beneficiary.getAccountHolderName(),
                    claimId
            );

            beneficiary.setRazorpayContactId(contactId);
            beneficiaries.save(beneficiary);
        }

        if (beneficiary.getRazorpayFundAccountId() == null) {

            String fundAccountId = razorpay.createFundAccount(
                    beneficiary.getRazorpayContactId(),
                    beneficiary.getAccountHolderName(),
                    beneficiary.getAccountNumber(),
                    beneficiary.getIfscCode()
            );

            beneficiary.setRazorpayFundAccountId(fundAccountId);
        }

        // Demo verification only, not real bank ownership validation.
        beneficiary.setVerificationStatus("DEMO_VERIFIED");

        return toResponse(beneficiaries.save(beneficiary));
    }

    private BankDetailsResponseDto toResponse(ClaimBeneficiary b) {

        String account = b.getAccountNumber();

        String masked = "*".repeat(Math.max(0, account.length() - 4))
                + account.substring(Math.max(0, account.length() - 4));

        return new BankDetailsResponseDto(
                b.getBeneficiaryId(),
                b.getClaimId(),
                b.getAccountHolderName(),
                masked,
                b.getIfscCode(),
                b.getBankName(),
                b.getVerificationStatus()
        );
    }
}