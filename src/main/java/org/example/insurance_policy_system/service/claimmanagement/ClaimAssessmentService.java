package org.example.insurance_policy_system.service.claimmanagement;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimAssessmentRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimAssessmentResponseDto;

public interface ClaimAssessmentService {

    ClaimAssessmentResponseDto createAssessment(
            Long claimId,
            ClaimAssessmentRequestDto requestDto
    );

    ClaimAssessmentResponseDto getAssessment(Long claimId);

    ClaimAssessmentResponseDto updateAssessment(
            Long claimId,
            ClaimAssessmentRequestDto requestDto
    );

}