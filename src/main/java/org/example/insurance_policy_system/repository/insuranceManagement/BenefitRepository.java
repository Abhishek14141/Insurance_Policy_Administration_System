package org.example.insurance_policy_system.repository.insuranceManagement;


import org.example.insurance_policy_system.entity.insuranceManagement.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BenefitRepository extends JpaRepository<Benefit,Long> { List<Benefit> findByProduct_ProductId(Long productId); }
