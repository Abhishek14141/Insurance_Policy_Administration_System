package org.example.insurance_policy_system.dto.insuranceManagement;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record ProductRequest(
 @NotNull Long categoryId,
 @NotBlank @Size(max=150) String productName,
 @NotBlank @Size(max=100) String productType,
 String description,
 @Min(0) @Max(120) Integer minimumAge,
 @Min(0) @Max(120) Integer maximumAge,
 @DecimalMin("0.00") BigDecimal minimumSumInsured,
 @DecimalMin("0.00") BigDecimal maximumSumInsured
) {}
