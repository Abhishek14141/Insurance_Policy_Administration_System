package org.example.insurance_policy_system.dto.quote;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRiderRequestDto {

    @NotNull(message = "Quote ID is required")
    @Positive(message = "Quote ID must be positive")
    private Long quoteId;

    @NotNull(message = "Rider ID is required")
    @Positive(message = "Rider ID must be positive")
    private Long riderId;
}