package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct, Long> {
    boolean existsByProductCode(String productCode);

}
