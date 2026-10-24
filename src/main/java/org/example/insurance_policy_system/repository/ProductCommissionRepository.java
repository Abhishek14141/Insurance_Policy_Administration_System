package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.ProductCommission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCommissionRepository extends JpaRepository<ProductCommission, Long> {
    Optional<ProductCommission> findByProduct_ProductId(Long productId);
}
