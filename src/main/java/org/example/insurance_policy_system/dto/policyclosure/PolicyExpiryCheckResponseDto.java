package org.example.insurance_policy_system.dto.policyclosure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyExpiryCheckResponseDto {
    private Long policyId;
    private String policyNo;
    private String policyStatus;
    private LocalDate expiryDate;
    private LocalDate checkedDate;
    private boolean eligibleForClosure;
    private String reason;
}
