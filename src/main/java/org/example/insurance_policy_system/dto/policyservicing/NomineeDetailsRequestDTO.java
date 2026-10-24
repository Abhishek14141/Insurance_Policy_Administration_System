package org.example.insurance_policy_system.dto.policyservicing;



import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NomineeDetailsRequestDTO {

    @NotNull(message = "Policy ID is required")
    private Long policyId;

    @NotBlank(message = "Nominee name is required")
    @Size(max = 100)
    private String nomineeName;

    @NotBlank(message = "Relationship is required")
    @Size(max = 50)
    private String relationship;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent
    private LocalDate dob;

    @NotNull(message = "Share percentage is required")
    @DecimalMin("0.01")
    @DecimalMax("100.00")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal sharePercentage;

    @Size(max = 100)
    private String guardianName;
}

