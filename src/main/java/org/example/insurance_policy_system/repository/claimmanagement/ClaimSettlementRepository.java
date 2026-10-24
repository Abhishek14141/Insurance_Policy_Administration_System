package org.example.insurance_policy_system.repository.claimmanagement;

import org.example.insurance_policy_system.entity.claimmanagement.ClaimSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClaimSettlementRepository  extends JpaRepository<ClaimSettlement , Long> {


    Optional<ClaimSettlement> findByClaimId_ClaimId(Long claimId);

}
