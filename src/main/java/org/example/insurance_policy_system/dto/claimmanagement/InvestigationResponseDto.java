package org.example.insurance_policy_system.dto.claimmanagement;

import lombok.*;
import org.example.insurance_policy_system.enums.claimmanagement.InvestigationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationResponseDto {

    private Long investigationId;
    private Long claimId;
    private Long investigatorId;
    private String investigationReason;
    private String reportPath;
    private InvestigationStatus investigationStatus;
}