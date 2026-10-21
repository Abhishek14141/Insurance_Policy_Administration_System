package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRejectionRequestDto {

    @NotBlank
    private String reason;
}