package org.example.insurance_policy_system.dto.policyservicing;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyCancellationRequestDTO {

    @NotBlank(message = "Cancellation reason is required")
    private String cancellationReason;
}

