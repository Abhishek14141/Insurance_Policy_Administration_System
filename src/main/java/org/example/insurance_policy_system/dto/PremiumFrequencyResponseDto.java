package org.example.insurance_policy_system.dto;

import lombok.Data;
import org.example.insurance_policy_system.enums.PremiumFrequency;

@Data
public class PremiumFrequencyResponseDto {
    private Long frequencyId;
    private PremiumFrequency frequencyName;
    private Integer installmentCount;
    private String status;
}
