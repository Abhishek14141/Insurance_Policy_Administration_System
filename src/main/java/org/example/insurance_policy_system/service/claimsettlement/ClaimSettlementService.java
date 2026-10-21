package org.example.insurance_policy_system.service.claimsettlement;

import org.example.insurance_policy_system.dto.claimsettlement.ClaimSettlementRequestDto;
import org.example.insurance_policy_system.dto.claimsettlement.ClaimSettlementResponseDto;

public interface ClaimSettlementService {

    ClaimSettlementResponseDto processSettlement(
            Long claimId,
            ClaimSettlementRequestDto request
    );

    ClaimSettlementResponseDto getSettlement(Long claimId);
}