package org.example.insurance_policy_system.dto.policyservicing;

import lombok.*;
import org.example.insurance_policy_system.enums.policyservicing.EndorsementApprovalStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyEndorsementResponseDTO {

    private Long endorsementId;
    private Long requestId;
    private String endorsementNumber;
    private String endorsementType;
    private String previousValue;
    private String updatedValue;
    private EndorsementApprovalStatus approvalStatus;
}

