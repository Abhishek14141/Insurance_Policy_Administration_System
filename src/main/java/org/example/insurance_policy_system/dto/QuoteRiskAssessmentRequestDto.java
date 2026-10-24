package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class QuoteRiskAssessmentRequestDto {
    @NotNull
    @Positive
    private Long quoteId;
}
