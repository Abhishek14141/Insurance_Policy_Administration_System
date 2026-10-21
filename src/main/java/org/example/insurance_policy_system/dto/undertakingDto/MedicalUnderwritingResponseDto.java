package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalUnderwritingResponseDto {
    private Long medicalId;
    private Long underwritingId;
    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal bmi;
    private String medicalHistory;
    private String medicalReportPath;
    private Boolean smoking;
    private Boolean alcoholConsumption;
    private Boolean adventureSports;
    private Boolean frequentInternationalTravel;
    private Boolean dangerousHobbies;
}
