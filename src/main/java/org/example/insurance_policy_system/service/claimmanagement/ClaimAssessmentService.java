package org.example.insurance_policy_system.service.claimmanagement;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimAssessmentRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimAssessmentResponseDto;

public interface ClaimAssessmentService {

    // Internal functionality: assessment after approval
    ClaimAssessmentResponseDto assessClaim(
            Long claimId,
            ClaimAssessmentRequestDto requestDto
    );

    // Retrieve existing assessment
    ClaimAssessmentResponseDto getAssessment(Long claimId);
}