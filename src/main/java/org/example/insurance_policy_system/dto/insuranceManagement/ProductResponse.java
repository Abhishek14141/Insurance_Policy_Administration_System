package org.example.insurance_policy_system.dto.insuranceManagement;
import java.math.BigDecimal;
public record ProductResponse(Long productId,String productCode,String productName,String productType,String description,
 Long categoryId,String categoryName,Integer minimumAge,Integer maximumAge,BigDecimal minimumSumInsured,
 BigDecimal maximumSumInsured,String status) {}
