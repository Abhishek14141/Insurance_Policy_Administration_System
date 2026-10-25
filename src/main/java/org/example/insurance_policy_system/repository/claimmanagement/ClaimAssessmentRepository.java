package org.example.insurance_policy_system.repository.claimmanagement;

import org.example.insurance_policy_system.entity.claimmanagement.ClaimAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimAssessmentRepository  extends JpaRepository<ClaimAssessment , Long> {

    boolean existsByClaim_ClaimId(Long claimId);

    List<ClaimAssessment> findByClaim_ClaimId(Long claimId);


}
