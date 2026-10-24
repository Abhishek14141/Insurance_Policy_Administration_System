package org.example.insurance_policy_system.dto;

import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private String description;
    private String status;
}
