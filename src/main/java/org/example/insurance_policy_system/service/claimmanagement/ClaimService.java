package org.example.insurance_policy_system.service.claimmanagement;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimResponseDto;

import java.util.List;

public interface ClaimService {

    // POST /api/v1/claims
    ClaimResponseDto registerClaim(ClaimRequestDto requestDto);

    // GET /api/v1/claims
    List<ClaimResponseDto> getAllClaims();

    // GET /api/v1/claims/{id}
    ClaimResponseDto getClaimById(Long claimId);

    // PUT /api/v1/claims/{id}
    ClaimResponseDto updateClaim(
            Long claimId,
            ClaimRequestDto requestDto
    );

    // DELETE /api/v1/claims/{id}
    void cancelClaim(Long claimId);
}