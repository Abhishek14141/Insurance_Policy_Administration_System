package org.example.insurance_policy_system.dto.customer;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class NomineeRequest {

    @NotBlank
    private String nomineeName;

    @NotBlank
    private String relationship;

    private LocalDate dateOfBirth;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("100.00")
    private BigDecimal sharePercentage;
}
