package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
