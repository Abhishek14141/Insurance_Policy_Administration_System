package org.example.insurance_policy_system.dto.claimsettlement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BankVerificationRequestDto {


    @NotBlank
  private   String accountHolderName;

    @NotBlank
    @Pattern(regexp = "\\d{9,18}")
  private   String accountNumber;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$")
 private    String ifscCode;



}



