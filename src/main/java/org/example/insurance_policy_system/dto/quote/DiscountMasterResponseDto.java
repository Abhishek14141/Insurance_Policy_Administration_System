package org.example.insurance_policy_system.dto.quote;

import com.springproject.insurance_policy_system_quote.enums.quote.DiscountMasterStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountMasterResponseDto {

    private Long discountMasterId;
    private String discountName;
    private Double discountPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private DiscountMasterStatus status;
}