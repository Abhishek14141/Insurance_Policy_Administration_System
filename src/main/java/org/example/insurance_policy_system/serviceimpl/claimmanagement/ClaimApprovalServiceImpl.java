
package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimmanagement.*;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimMaster;
import org.example.insurance_policy_system.enums.claimmanagement.ClaimStatus;
import org.example.insurance_policy_system.repository.claimmanagement.ClaimMasterRepository;
import org.example.insurance_policy_system.repository.claimmanagement.ClaimAssessmentRepository;
import org.example.insurance_policy_system.service.claimmanagement.ClaimApprovalService;
import org.example.insurance_policy_system.service.claimmanagement.ClaimHistoryService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClaimApprovalServiceImpl implements ClaimApprovalService {

    private static final Long TEST_EMPLOYEE_ID = 1L;

    private final ClaimMasterRepository claimRepository;
    private final ClaimAssessmentRepository assessmentRepository;
    private final ClaimHistoryService historyService;

    @Override
    @Transactional
    public ClaimResponseDto approveClaim(
            Long claimId,
            ClaimApprovalRequestDto requestDto) {

        ClaimMaster claim = findClaim(claimId);

        validateDecisionAllowed(claim);

        var assessments = assessmentRepository
                .findByClaim_ClaimId(claimId);

        if (assessments.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Claim assessment must be completed before approval"
            );
        }

        claim.setClaimStatus(ClaimStatus.APPROVED);

        ClaimMaster saved = claimRepository.saveAndFlush(claim);

        historyService.recordHistory(
                claimId,
                "CLAIM_APPROVED",
                TEST_EMPLOYEE_ID,
                "Claim approved after assessment. Net payable: "
                        + assessments.get(0).getNetPayable()
        );

        return toResponse(saved);
    }

    @Override
    @Transactional
    public ClaimResponseDto rejectClaim(
            Long claimId,
            ClaimRejectionRequestDto requestDto) {

        ClaimMaster claim = findClaim(claimId);

        validateDecisionAllowed(claim);

        // Replace with your DTO's actual rejection reason getter
        String reason = requestDto.getReason();

        if (reason == null || reason.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rejection reason is mandatory"
            );
        }

        claim.setClaimStatus(ClaimStatus.REJECTED);

        ClaimMaster saved = claimRepository.saveAndFlush(claim);

        historyService.recordHistory(
                claimId,
                "CLAIM_REJECTED",
                TEST_EMPLOYEE_ID,
                reason
        );

        return toResponse(saved);
    }

    @Override
    @Transactional
    public ClaimResponseDto updateClaimDecision(
            Long claimId,
            ClaimDecisionUpdateRequestDto requestDto) {

        ClaimMaster claim = findClaim(claimId);

        validateDecisionAllowed(claim);

        if (requestDto == null
                || requestDto.getAssessmentId() == null
                || requestDto.getRemarks() == null
                || requestDto.getRemarks().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Assessment ID and remarks are required"
            );
        }

        var assessment = assessmentRepository.findById(
                requestDto.getAssessmentId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Assessment not found"
        ));

        if (!assessment.getClaim().getClaimId().equals(claimId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Assessment does not belong to this claim"
            );
        }

        historyService.recordHistory(
                claimId,
                "CLAIM_DECISION_REVIEW_UPDATED",
                TEST_EMPLOYEE_ID,
                requestDto.getRemarks()
        );

        return toResponse(claim);
    }

    private ClaimMaster findClaim(Long claimId) {
        return claimRepository.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Claim not found: " + claimId
                ));
    }

    private void validateDecisionAllowed(ClaimMaster claim) {

        ClaimStatus status = claim.getClaimStatus();

        if (status == ClaimStatus.APPROVED
                || status == ClaimStatus.REJECTED
                || status == ClaimStatus.SETTLED) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Claim decision cannot be changed in status: " + status
            );
        }
    }
    private ClaimResponseDto toResponse(ClaimMaster claim) {

        ClaimResponseDto dto = new ClaimResponseDto();

        dto.setClaimId(claim.getClaimId());
        dto.setClaimNumber(claim.getClaimNumber());
        dto.setClaimStatus(claim.getClaimStatus());

        return dto;
    }
}
