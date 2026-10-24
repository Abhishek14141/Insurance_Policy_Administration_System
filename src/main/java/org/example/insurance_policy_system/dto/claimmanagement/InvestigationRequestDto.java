package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationRequestDto {

    @NotNull
    private Long investigatorId;

    @NotBlank
    private String investigationReason;
}