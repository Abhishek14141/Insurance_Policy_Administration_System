package org.example.insurance_policy_system.service.claimmanagement;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimApprovalRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimDecisionUpdateRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimRejectionRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimResponseDto;

public interface ClaimApprovalService {

    // PUT /api/v1/claims/{id}/approve
    ClaimResponseDto approveClaim(
            Long claimId,
            ClaimApprovalRequestDto requestDto
    );

    // PUT /api/v1/claims/{id}/reject
    ClaimResponseDto rejectClaim(
            Long claimId,
            ClaimRejectionRequestDto requestDto
    );


    ClaimResponseDto updateClaimDecision(
            Long claimId,
            ClaimDecisionUpdateRequestDto requestDto
    );
}