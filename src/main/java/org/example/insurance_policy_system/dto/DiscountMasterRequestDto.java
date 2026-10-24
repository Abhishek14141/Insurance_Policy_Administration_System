package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountMasterRequestDto {
    @NotBlank
    private String discountName;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal discountPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
}
