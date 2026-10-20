package org.example.insurance_policy_system.repository.quote;


import com.springproject.insurance_policy_system.entity.quote.PremiumCalculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumCalculationRepository extends JpaRepository<PremiumCalculation, Long> {
}
