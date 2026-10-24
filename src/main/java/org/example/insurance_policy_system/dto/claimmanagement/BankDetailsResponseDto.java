package org.example.insurance_policy_system.dto.claimmanagement;

public record BankDetailsResponseDto(
        Long beneficiaryId,
        Long claimId,
        String accountHolderName,
        String maskedAccountNumber,
        String ifscCode,
        String bankName,
        String verificationStatus
) {}