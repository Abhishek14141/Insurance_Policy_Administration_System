package org.example.insurance_policy_system.dto.undertakingDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UnderwritingRequestDto {

    @NotNull
    @Positive
    private Long quoteId;

    @NotNull
    @Positive
    private Long customerId;

    @NotNull
    @Positive
    private Long productId;
}