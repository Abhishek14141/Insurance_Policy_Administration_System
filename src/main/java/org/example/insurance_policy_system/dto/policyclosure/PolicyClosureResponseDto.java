
package org.example.insurance_policy_system.dto.policyclosure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.entity.policyclosure.PolicyClosureStatus;
import org.example.insurance_policy_system.entity.policyclosure.PolicyClosureType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyClosureResponseDto {

    private Long closureId;
    private String closureNumber;
    private Long policyId;
    private PolicyClosureType closureType;
    private PolicyClosureStatus closureStatus;
    private LocalDate policyExpiryDate;
    private Long closedBy;
    private LocalDateTime closureDate;
    private String remarks;
}
