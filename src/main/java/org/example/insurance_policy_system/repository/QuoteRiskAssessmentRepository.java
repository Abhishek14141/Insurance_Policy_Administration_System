package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.QuoteRiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuoteRiskAssessmentRepository extends JpaRepository<QuoteRiskAssessment, Long> {
    Optional<QuoteRiskAssessment> findFirstByQuote_QuoteIdOrderByRiskIdDesc(Long quoteId);
}
