package org.example.insurance_policy_system.dto.insuranceManagement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FrequencyRequest(@NotBlank @Size(max=30) String frequencyName, @NotNull @Min(1) Integer installmentCount, String status) {}
