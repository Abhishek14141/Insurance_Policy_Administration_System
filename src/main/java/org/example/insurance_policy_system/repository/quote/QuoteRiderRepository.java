package org.example.insurance_policy_system.repository.quote;


import org.example.insurance_policy_system.entity.quote.PremiumCalculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRiderRepository extends JpaRepository<PremiumCalculation, Long> {
}
