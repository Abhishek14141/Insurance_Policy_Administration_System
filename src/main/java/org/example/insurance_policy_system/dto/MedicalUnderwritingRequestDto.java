package org.example.insurance_policy_system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalUnderwritingRequestDto {
    @DecimalMin("1.00")
    private BigDecimal height;
    @DecimalMin("1.00")
    private BigDecimal weight;
    private String bloodPressure;
    private Boolean diabetes;
    private Boolean heartDisease;
    private Boolean cancerHistory;
    private Integer age;
    private String medicalHistory;
    private String medicalReportPath;

}
