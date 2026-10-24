package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import org.example.insurance_policy_system.enums.PremiumFrequency;

@Data
public class PremiumFrequencyRequestDto {
    @NotNull
    private PremiumFrequency frequencyName;
    @NotNull
    @Min(1)
    private Integer installmentCount;
}
