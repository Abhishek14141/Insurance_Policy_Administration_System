package org.example.insurance_policy_system.controller.claimmanagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimApprovalRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimRejectionRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimDecisionUpdateRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimResponseDto;
import org.example.insurance_policy_system.service.claimmanagement.ClaimApprovalService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class ClaimApprovalController {

    private final ClaimApprovalService claimApprovalService;

    // 1. APPROVE CLAIM
    @PutMapping("/{claimId}/approve")
    public ResponseEntity<ClaimResponseDto> approveClaim(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimApprovalRequestDto requestDto) {

        ClaimResponseDto response =
                claimApprovalService.approveClaim(claimId, requestDto);

        return ResponseEntity.ok(response);
    }

    // 2. REJECT CLAIM
    @PutMapping("/{claimId}/reject")
    public ResponseEntity<ClaimResponseDto> rejectClaim(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimRejectionRequestDto requestDto) {

        ClaimResponseDto response =
                claimApprovalService.rejectClaim(claimId, requestDto);

        return ResponseEntity.ok(response);
    }

    // 3. UPDATE CLAIM DECISION REVIEW
    @PutMapping("/{claimId}/decision")
    public ResponseEntity<ClaimResponseDto> updateClaimDecision(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimDecisionUpdateRequestDto requestDto) {

        ClaimResponseDto response =
                claimApprovalService.updateClaimDecision(
                        claimId, requestDto
                );

        return ResponseEntity.ok(response);
    }
}