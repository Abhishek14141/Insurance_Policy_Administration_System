package org.example.insurance_policy_system.dto;

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
    private String bloodPressure;
    private Boolean diabetes;
    private Boolean heartDisease;
    private Boolean cancerHistory;
    private Integer age;

    private String medicalHistory;
    private String medicalReportPath;
}
