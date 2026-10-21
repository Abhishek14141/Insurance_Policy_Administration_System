package org.example.insurance_policy_system.dto.kyc;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record KycDecisionRequest(Long performedBy, @Size(max=500) String remarks, @NotBlank String reason) {}
