package org.example.insurance_policy_system.dto.insuranceManagement;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.example.insurance_policy_system.enums.insuranceManagement.RiderStatus;

import java.math.BigDecimal;

public record RiderRequest(
        @NotBlank
        @Size(max = 150)
        String riderName,

        String riderDescription,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal riderPremium,

        RiderStatus status
) {
}
