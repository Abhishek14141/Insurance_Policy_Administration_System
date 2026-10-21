package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyReportRequestDto {

    @NotNull
    private LocalDateTime inspectionDate;

    @NotBlank
    private String reportPath;
}