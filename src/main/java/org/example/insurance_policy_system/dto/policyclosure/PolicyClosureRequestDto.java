
package org.example.insurance_policy_system.dto.policyclosure;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyClosureRequestDto {

    @Positive(message = "Closed by user ID must be positive")
    private Long closedBy;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    private String remarks;
}
