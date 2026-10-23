package org.example.insurance_policy_system.service.claimmanagement;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimHistoryResponseDto;

import java.util.List;

public interface ClaimHistoryService {

    // Called internally after important claim actions
    void recordHistory(
            Long claimId,
            String action,
            Long performedBy,
            String remarks
    );

    // Retrieve claim audit history
   // List<ClaimHistoryResponseDto> getClaimHistory(Long claimId);
}