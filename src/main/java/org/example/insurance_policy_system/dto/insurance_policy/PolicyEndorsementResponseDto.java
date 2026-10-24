package org.example.insurance_policy_system.dto.insurance_policy;

import com.springproject.insurance_policy_system_policyinsurance.enums.insurance_policy.EndorsementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEndorsementResponseDto {

    private Long endorsementId;
    private Long policyId;
    private EndorsementType endorsementType;
    private String oldValue;
    private String newValue;
    private Long approvedBy;
    private LocalDateTime endorsementDate;
}