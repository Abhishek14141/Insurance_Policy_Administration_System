package org.example.insurance_policy_system.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AvailableRiderResponseDto {
    private Long riderId;
    private String riderName;
    private String riderDescription;
    private BigDecimal riderPremium;
}