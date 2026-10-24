package org.example.insurance_policy_system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerResponse {
    private Long customerId;
    private String occupation;
    private BigDecimal annualIncome;
    private LocalDate dateOfBirth;
    private Integer age;
}
