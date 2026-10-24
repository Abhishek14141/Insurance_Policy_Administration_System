package org.example.insurance_policy_system.dto.kyc;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
public record KycIdentityRequest(
        @Pattern(regexp="^$|^[0-9]{12}$", message="Aadhaar must contain exactly 12 digits") String aadhaarNumber,
        @Size(max=150) String aadhaarName,
        LocalDate aadhaarDateOfBirth,
        @Size(max=500) String aadhaarAddress,
        @Pattern(regexp="^$|^[A-Za-z]{5}[0-9]{4}[A-Za-z]$", message="PAN format must be ABCDE1234F") String panNumber,
        @Size(max=150) String panHolderName) {}
