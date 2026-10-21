
package org.example.insurance_policy_system.dto.investigation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteInvestigationRequest {

    @NotBlank(message = "Investigation report path is required")
    @Size(max = 500, message = "Report path cannot exceed 500 characters")
    private String reportPath;
}
