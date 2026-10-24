package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment,Long> {
    Optional<RiskAssessment> findByUnderwritingCase_UnderwritingId(Long underwritingId);}
