package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
public class ProductCommissionRequestDto {
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal firstYearCommission;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal renewalCommission;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal branchCommission;
}
