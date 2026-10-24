package org.example.insurance_policy_system.dto;

import lombok.Data;

import java.math.BigDecimal;

import org.example.insurance_policy_system.enums.ProductType;

@Data
public class ProductResponseDto {
    private Long productId;
    private String productCode;
    private String productName;
    private ProductType productType;
    private Long categoryId;
    private String categoryName;
    private String description;
    private Integer minimumAge;
    private Integer maximumAge;
    private BigDecimal minSumInsured;
    private BigDecimal maxSumInsured;
    private Integer minTermYears;
    private Integer maxTermYears;
    private BigDecimal productRate;
    private String status;
}
