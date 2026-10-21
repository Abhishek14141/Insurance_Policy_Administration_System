package org.example.insurance_policy_system.dto.kyc;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record KycDocumentRequest(@NotNull Long kycId, @NotBlank String documentType, @NotBlank String documentNumber, @Size(max=500) String remarks) {}
