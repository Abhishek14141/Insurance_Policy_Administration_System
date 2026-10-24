package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalUnderwritingRequestDto {

    private BigDecimal height;
    private BigDecimal weight;
    private String bloodPressure;
    private Boolean diabetes;
    private Boolean heartDisease;
    private Boolean cancerHistory;
    private BigDecimal bmi;
    private Integer age;
    private String medicalHistory;
    private MultipartFile medicalReportPath;


}
