package org.example.insurance_policy_system.service.claimmanagement;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimDocumentRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimDocumentResponseDto;
import org.example.insurance_policy_system.dto.claimmanagement.DocumentVerificationRequestDto;

import java.util.List;

public interface ClaimDocumentService {

    // POST /api/v1/claims/{id}/documents
    ClaimDocumentResponseDto uploadDocument(
            Long claimId,
            ClaimDocumentRequestDto requestDto
    );

    // GET /api/v1/claims/{id}/documents
    List<ClaimDocumentResponseDto> getClaimDocuments(Long claimId);

    // PUT /api/v1/claims/{id}/documents/verify
    ClaimDocumentResponseDto verifyDocument(
            Long claimId,
            DocumentVerificationRequestDto requestDto
    );
}