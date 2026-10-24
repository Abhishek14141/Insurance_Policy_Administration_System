package org.example.insurance_policy_system.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentRequest {

    @NotBlank
    private String documentType;

    @NotBlank
    private String fileReference;
}
