package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CategoryRequestDto {
    @NotBlank
    private String categoryName;
    private String description;
}
