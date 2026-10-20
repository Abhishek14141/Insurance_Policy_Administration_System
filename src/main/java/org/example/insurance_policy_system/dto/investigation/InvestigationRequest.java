
package org.example.insurance_policy_system.dto.investigation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestigationRequest {

    @NotNull(message = "Investigator ID is required")
    @Positive(message = "Investigator ID must be greater than zero")
    private Long investigatorId;

    @NotBlank(message = "Investigation reason is required")
    @Size(max = 2000, message = "Investigation reason cannot exceed 2000 characters")
    private String investigationReason;
}
