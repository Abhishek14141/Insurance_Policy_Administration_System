package org.example.insurance_policy_system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingDecisionRequestDto {
    @NotNull
    @Positive
    private Long approvedBy;
    private String rejectionReason;
}
