package org.example.insurance_policy_system.repository.claimmanagement;

import org.example.insurance_policy_system.entity.claimmanagement.ClaimAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimAssessmentRepository  extends JpaRepository<ClaimAssessment , Long> {
}
