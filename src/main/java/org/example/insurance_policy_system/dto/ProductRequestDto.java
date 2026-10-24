package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import org.example.insurance_policy_system.enums.ProductType;

@Data
public class ProductRequestDto {
    @NotBlank
    private String productCode;
    @NotBlank
    private String productName;
    @NotNull
    private ProductType productType;
    @NotNull
    @Positive
    private Long categoryId;
    private String description;
    @Min(0)
    private Integer minimumAge;
    @Min(0)
    private Integer maximumAge;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal minSumInsured;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal maxSumInsured;
    @Min(1)
    private Integer minTermYears;
    @Min(1)
    private Integer maxTermYears;
    @DecimalMin("0.001")
    private BigDecimal productRate;
}
