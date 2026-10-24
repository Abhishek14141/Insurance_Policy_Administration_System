package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoverageRepository extends JpaRepository<Coverage, Long> {
    List<Coverage> findByProduct_ProductId(Long productId);
}
