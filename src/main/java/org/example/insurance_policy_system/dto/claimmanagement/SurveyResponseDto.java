package org.example.insurance_policy_system.dto.claimmanagement;

import lombok.*;
import org.example.insurance_policy_system.enums.claimmanagement.SurveyStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDto {

    private Long surveyId;
    private Long claimId;
    private Long surveyorId;
    private LocalDateTime assignedDate;
    private LocalDateTime inspectionDate;
    private String reportPath;
    private SurveyStatus surveyStatus;
}