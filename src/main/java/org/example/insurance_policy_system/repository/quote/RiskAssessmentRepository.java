package org.example.insurance_policy_system.repository.quote;


import org.example.insurance_policy_system.entity.quote.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
}
