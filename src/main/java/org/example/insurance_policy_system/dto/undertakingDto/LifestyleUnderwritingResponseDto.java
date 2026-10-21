package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.Data;

@Data
public class LifestyleUnderwritingResponseDto {
    private Long caseId;
    private Long medicalId;

    private Boolean smoking;
    private Boolean alcoholConsumption;
    private Boolean adventureSports;
    private Boolean frequentInternationalTravel;
    private Boolean dangerousHobbies;
}

