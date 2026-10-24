package org.example.insurance_policy_system.dto.customer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class NomineeResponse {
    private Long nomineeId;
    private Long customerId;
    private String nomineeName;
    private String relationship;
    private LocalDate dateOfBirth;
    private BigDecimal sharePercentage;
}
