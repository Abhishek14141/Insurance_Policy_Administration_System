package org.example.insurance_policy_system.dto.insurance_policy;

import com.springproject.insurance_policy_system_policyinsurance.enums.insurance_policy.EndorsementType;
import jakarta.validation.constraints.NotBlank;
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
public class PolicyEndorsementRequestDto {

    @NotNull(message = "Policy ID is required")
    @Positive(message = "Policy ID must be positive")
    private Long policyId;

    @NotNull(message = "Endorsement type is required")
    private EndorsementType endorsementType;

    @NotBlank(message = "Old value is required")
    private String oldValue;

    @NotBlank(message = "New value is required")
    private String newValue;
}