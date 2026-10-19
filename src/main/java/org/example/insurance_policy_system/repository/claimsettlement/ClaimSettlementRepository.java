package org.example.insurance_policy_system.repository.claimsettlement;

import org.example.insurance_policy_system.entity.claimsettlement.ClaimSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimSettlementRepository  extends JpaRepository<ClaimSettlement , Long> {
}
