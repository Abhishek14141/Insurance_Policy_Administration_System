package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.PremiumFrequencyMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumFrequencyRepository extends JpaRepository<PremiumFrequencyMaster, Long> {
}
