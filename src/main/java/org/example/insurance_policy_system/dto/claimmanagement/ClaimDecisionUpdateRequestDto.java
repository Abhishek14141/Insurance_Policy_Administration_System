package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimDecisionUpdateRequestDto {

    @NotNull
    private Long assessmentId;

    @NotBlank
    private String remarks;
}