
package org.example.insurance_policy_system.dto.investigation;

import org.example.insurance_policy_system.enums.investigation.InvestigationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestigationResponse {

    private Long investigationId;
    private Long claimId;
    private Long investigatorId;
    private String investigationReason;
    private String reportPath;
    private InvestigationStatus investigationStatus;
}
