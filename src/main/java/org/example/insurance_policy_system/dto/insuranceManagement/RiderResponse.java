package org.example.insurance_policy_system.dto.insuranceManagement;

import org.example.insurance_policy_system.enums.insuranceManagement.RiderStatus;
import java.math.BigDecimal;

public record RiderResponse(
        Long riderId,
        Long productId,
        String riderName,
        String riderDescription,
        BigDecimal riderPremium,
        RiderStatus status
) {
}
