package org.example.insurance_policy_system.repository.insuranceManagement;

import org.example.insurance_policy_system.entity.insuranceManagement.PremiumFrequency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumFrequencyRepository extends JpaRepository<PremiumFrequency,Long> { boolean existsByFrequencyNameIgnoreCase(String frequencyName); }
