package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.RiskLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessmentResponseDto {

    private Long riskId;
    private Long underwritingId;
    private Integer ageScore;
    private Integer medicalScore;
    private Integer occupationScore;
    private Integer lifestyleScore;
    private Integer financialScore;
    private Integer totalScore;
    private RiskLevel riskCategory;
}
