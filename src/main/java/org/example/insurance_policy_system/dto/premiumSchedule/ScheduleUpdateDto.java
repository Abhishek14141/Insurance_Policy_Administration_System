package org.example.insurance_policy_system.dto.premiumSchedule;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScheduleUpdateDto {
    private String status;
    private BigDecimal lateFee;
}
