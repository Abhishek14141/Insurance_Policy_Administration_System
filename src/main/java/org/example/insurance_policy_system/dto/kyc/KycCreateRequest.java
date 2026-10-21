package org.example.insurance_policy_system.dto.kyc;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
public record KycCreateRequest(@NotNull Long customerId, LocalDate kycDate, @Size(max=500) String remarks) {}
