package org.example.insurance_policy_system.repository.claimmanagement;

import org.example.insurance_policy_system.entity.claimmanagement.ClaimBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClaimBeneficiaryRepository
        extends JpaRepository<ClaimBeneficiary, Long> {

    Optional<ClaimBeneficiary> findByClaimId(Long claimId);
}