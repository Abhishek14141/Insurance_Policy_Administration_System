package org.example.insurance_policy_system.dto.insuranceManagement;
import org.example.insurance_policy_system.enums.insuranceManagement.ProductStatus;
import org.example.insurance_policy_system.enums.insuranceManagement.ProductType;

import java.math.BigDecimal;
public record ProductResponse (
        Long productId,
        String productCode,
        String productName,
        ProductType productType,
        String description,
        Long categoryId,
        String categoryName,
        Integer minimumAge,
        Integer maximumAge,
        BigDecimal minimumSumInsured,
        BigDecimal maximumSumInsured,
        Integer minimumPolicyTermYears,
        Integer maximumPolicyTermYears,
        ProductStatus status
) {
        }
