package org.example.insurance_policy_system.service.claimmanagement;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimSettlementRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimSettlementResponseDto;

public interface ClaimSettlementService {

    ClaimSettlementResponseDto processSettlement(
            Long claimId,
            ClaimSettlementRequestDto request
    );

    ClaimSettlementResponseDto getSettlement(Long claimId);
}