package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalUnderwritingRequestDto {
    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal bmi;
    private Integer age;
    private String medicalHistory;
    private String medicalReportPath;


}
