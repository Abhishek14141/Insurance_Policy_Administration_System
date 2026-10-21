package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequestDto {

    @NotNull
    private Long policyId;

    @NotNull
    private Long customerId;

    @NotBlank
    private String claimType;

    @NotNull
    @PastOrPresent
    private LocalDate incidentDate;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal estimatedLoss;
}