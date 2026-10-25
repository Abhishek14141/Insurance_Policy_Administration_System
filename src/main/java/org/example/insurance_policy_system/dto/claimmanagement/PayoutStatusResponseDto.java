package org.example.insurance_policy_system.dto.claimmanagement;

import java.math.BigDecimal;

public record PayoutStatusResponseDto(
        Long settlementId,
        Long claimId,
        BigDecimal amount,
        String paymentMode,
        String settlementStatus,
        String providerStatus,
        String payoutId,
        String transactionReference,
        String adviceNumber
) {}