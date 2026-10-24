package org.example.insurance_policy_system.dto.customer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.insurance_policy_system.enums.customer.EmploymentType;
import org.example.insurance_policy_system.enums.customer.Occupation;
import org.example.insurance_policy_system.enums.customer.PreferredContactMode;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerRegistrationRequest {
    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotBlank
    private String gender;

    @NotBlank
    private String maritalStatus;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must contain 10 digits")
    private String mobileNumber;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Occupation occupation;

    private String employerName;

    @NotNull
    private EmploymentType employmentType;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal annualIncome;

    @NotNull
    private PreferredContactMode preferredContactMode;
}
