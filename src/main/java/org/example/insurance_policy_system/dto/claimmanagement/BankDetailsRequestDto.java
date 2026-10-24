package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BankDetailsRequestDto {

    @NotBlank
    private String accountHolderName;

    @NotBlank
    @Pattern(regexp = "[0-9]{9,18}")
    private String accountNumber;

    @NotBlank
    private String confirmAccountNumber;

    @NotBlank
    @Pattern(regexp = "(?i)[A-Z]{4}0[A-Z0-9]{6}")
    private String ifscCode;

    @NotBlank
    private String bankName;
}