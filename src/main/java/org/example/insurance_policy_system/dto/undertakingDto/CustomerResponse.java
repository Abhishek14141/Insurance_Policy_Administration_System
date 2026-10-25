package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerResponse {
    private Long customerId;
    private String occupation;
    private BigDecimal annualIncome;
}
