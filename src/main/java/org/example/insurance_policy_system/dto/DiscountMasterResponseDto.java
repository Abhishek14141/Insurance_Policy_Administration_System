package org.example.insurance_policy_system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.example.insurance_policy_system.enums.DiscountStatus;

@Data
public class DiscountMasterResponseDto {
    private Long discountId;
    private String discountName;
    private BigDecimal discountPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private DiscountStatus status;
}
