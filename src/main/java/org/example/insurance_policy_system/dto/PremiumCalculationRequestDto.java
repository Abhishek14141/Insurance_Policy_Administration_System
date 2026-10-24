package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Data
public class PremiumCalculationRequestDto {
    @NotNull
    @Positive
    private Long quoteId;
    private List<Long> riderIds;
    private Long discountId;
}
