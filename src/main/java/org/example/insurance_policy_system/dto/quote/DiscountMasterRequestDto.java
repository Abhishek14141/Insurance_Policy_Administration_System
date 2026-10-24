package org.example.insurance_policy_system.dto.quote;

import com.springproject.insurance_policy_system_quote.enums.quote.DiscountMasterStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountMasterRequestDto {

    @NotBlank(message = "Discount name is required")
    private String discountName;

    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount percentage must be greater than 0")
    @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100")
    private Double discountPercentage;

    @NotNull(message = "Valid from date is required")
    private LocalDate validFrom;

    @NotNull(message = "Valid to date is required")
    private LocalDate validTo;

    @NotNull(message = "Discount status is required")
    private DiscountMasterStatus status;

    @AssertTrue(message = "Valid to date must be after or equal to valid from date")
    public boolean isValidDateRange() {
        return validFrom == null || validTo == null || !validTo.isBefore(validFrom);
    }
}