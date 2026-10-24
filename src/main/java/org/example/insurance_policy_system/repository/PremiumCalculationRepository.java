package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.PremiumCalculation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PremiumCalculationRepository extends JpaRepository<PremiumCalculation, Long> {
    Optional<PremiumCalculation> findFirstByQuote_QuoteIdOrderByCalculatedAtDesc(Long quoteId);
}
