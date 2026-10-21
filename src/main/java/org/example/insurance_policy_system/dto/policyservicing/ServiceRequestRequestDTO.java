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
public class ServiceRequestRequestDTO {

    @NotNull(message = "Policy ID is required")
    private Long policyId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Request type is required")
    @Size(max = 100)
    private String requestType;
}

