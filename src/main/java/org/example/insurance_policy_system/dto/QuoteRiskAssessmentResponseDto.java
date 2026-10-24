package org.example.insurance_policy_system.dto;

import lombok.Data;
import org.example.insurance_policy_system.enums.QuoteRiskCategory;

@Data
public class QuoteRiskAssessmentResponseDto {
    private Long riskId;
    private Long quoteId;
    private Integer ageScore;
    private Integer occupationScore;
    private Integer medicalScore;
    private Integer lifestyleScore;
    private Integer  FinancialScore;
    private Integer totalScore;
    private QuoteRiskCategory riskCategory;
}
