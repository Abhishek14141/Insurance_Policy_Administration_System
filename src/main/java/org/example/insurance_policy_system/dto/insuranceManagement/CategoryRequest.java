package org.example.insurance_policy_system.dto.insuranceManagement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record CategoryRequest(@NotBlank @Size(max=100) String categoryName, @Size(max=255) String description) {}
