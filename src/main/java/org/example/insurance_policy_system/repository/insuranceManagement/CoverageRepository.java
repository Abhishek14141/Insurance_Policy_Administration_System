package org.example.insurance_policy_system.repository.insuranceManagement;

import org.example.insurance_policy_system.entity.insuranceManagement.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoverageRepository extends JpaRepository<Coverage,Long> {  }
