package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.Data;

@Data
public class LifestyleUnderwritingRequestDto {
    private Boolean smoking;
    private Boolean alcoholConsumption;
    private Boolean adventureSports;
    private Boolean frequentInternationalTravel;
    private Boolean dangerousHobbies;
}
