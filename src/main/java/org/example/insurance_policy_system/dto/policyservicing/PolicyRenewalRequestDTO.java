package org.example.insurance_policy_system.dto.policyservicing;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyRenewalRequestDTO {

    @NotNull(message = "New expiry date is required")
    @Future(message = "New expiry date must be in the future")
    private LocalDate newExpiryDate;
}

