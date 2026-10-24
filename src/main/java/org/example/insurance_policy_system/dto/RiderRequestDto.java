package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
public class RiderRequestDto {
    @NotBlank
    private String riderName;
    private String riderDescription;
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal riderPremium;
}
