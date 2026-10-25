package org.example.insurance_policy_system.dto.premiumSchedule;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequestDto {
    private Long policyId;
    private String premiumFrequency;
    private Integer policyTermMonths;
    private BigDecimal finalPremium;

    @NotNull(message = "Policy start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;
    private Long customerId;
}
