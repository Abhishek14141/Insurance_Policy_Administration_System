package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAssignmentRequestDto {

    @NotNull
    private Long surveyorId;
}