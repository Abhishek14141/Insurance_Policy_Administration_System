package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
public class BenefitRequestDto {
    @NotBlank
    private String benefitName;
    private String benefitDescription;
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal benefitLimit;
}
