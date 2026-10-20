package org.example.insurance_policy_system.repository.insuranceManagement;

import org.example.insurance_policy_system.entity.insuranceManagement.ProductCommission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCommissionRepository extends JpaRepository<ProductCommission,Long> { Optional<ProductCommission> findByProduct_ProductId(Long productId); }
