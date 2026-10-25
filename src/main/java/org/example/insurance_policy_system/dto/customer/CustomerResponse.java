package org.example.insurance_policy_system.dto.customer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerResponse {
    private Long customerId;
    private String customerCode;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String mobileNumber;
    private String email;
    private String preferredContactMode;
    private String occupation;
    private String employerName;
    private String employmentType;
    private BigDecimal annualIncome;
    private String customerStatus;
}
