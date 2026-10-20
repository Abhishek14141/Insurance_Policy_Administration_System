package org.example.insurance_policy_system.repository.insuranceManagement;
import org.example.insurance_policy_system.entity.insuranceManagement.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> { boolean existsByCategoryNameIgnoreCase(String categoryName); }
