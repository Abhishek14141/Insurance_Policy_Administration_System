package org.example.insurance_policy_system.repository.insuranceManagement;

import org.example.insurance_policy_system.entity.insuranceManagement.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct,Long> {
    boolean existsByProductNameIgnoreCase(String productName);
    boolean existsByProductNameIgnoreCaseAndProductIdNot(String productName, Long productId);
    List<InsuranceProduct> findByStatusIgnoreCase(String status);
    long countByStatusIgnoreCase(String status);
}
