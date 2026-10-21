package org.example.insurance_policy_system.dto.quote;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteHistoryRequestDto {

    @NotNull(message = "Quote ID is required")
    @Positive(message = "Quote ID must be positive")
    private Long quoteId;

    @NotBlank(message = "Action is required")
    private String action;

    @NotNull(message = "Performed by employee ID is required")
    @Positive(message = "Employee ID must be positive")
    private Long performedBy;

    @Size(max = 500, message = "Remark cannot exceed 500 characters")
    private String remark;
}