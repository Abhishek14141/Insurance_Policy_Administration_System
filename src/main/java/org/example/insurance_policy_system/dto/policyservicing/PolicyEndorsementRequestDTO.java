package org.example.insurance_policy_system.dto.policyservicing;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyEndorsementRequestDTO {

    @NotNull(message = "Request ID is required")
    private Long requestId;

    @NotBlank(message = "Endorsement type is required")
    @Size(max = 100)
    private String endorsementType;

    private String previousValue;

    private String updatedValue;
}

