package org.example.insurance_policy_system.dto.claimsettlement;

public record BankVerificationResponseDto(
        Long claimId,
        String verificationStatus,
        String verificationReference,
        String message
) {}