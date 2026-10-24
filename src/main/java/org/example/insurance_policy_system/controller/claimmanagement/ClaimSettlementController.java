package org.example.insurance_policy_system.controller.claimmanagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimmanagement.*;
import org.example.insurance_policy_system.service.claimmanagement.ClaimSettlementService;
import org.example.insurance_policy_system.serviceimpl.claimmanagement.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ClaimSettlementController {

    private final ClaimSettlementService settlementService;
    private final ClaimSettlementServiceImpl settlementImpl;
    private final SettlementAdviceServiceImpl adviceService;

    @PostMapping("/claims/{claimId}/settlement")
    public ResponseEntity<ClaimSettlementResponseDto> createSettlement(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimSettlementRequestDto request) {

        return ResponseEntity.ok(
                settlementService.processSettlement(claimId, request));
    }

    @GetMapping("/claims/{claimId}/settlement")
    public ResponseEntity<ClaimSettlementResponseDto> getSettlement(
            @PathVariable Long claimId) {

        return ResponseEntity.ok(
                settlementService.getSettlement(claimId));
    }

    @PostMapping("/settlements/{settlementId}/disburse")
    public ResponseEntity<PayoutStatusResponseDto> disburse(
            @PathVariable Long settlementId) {

        return ResponseEntity.ok(
                settlementImpl.disburse(settlementId));
    }

    @GetMapping("/settlements/{settlementId}/status")
    public ResponseEntity<PayoutStatusResponseDto> checkStatus(
            @PathVariable Long settlementId) {

        return ResponseEntity.ok(
                settlementImpl.checkPayoutStatus(settlementId));
    }

    @GetMapping(
            value = "/settlements/{settlementId}/advice",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> downloadAdvice(
            @PathVariable Long settlementId) {

        byte[] pdf = adviceService.generatePdf(settlementId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=settlement-advice-"
                                + settlementId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}