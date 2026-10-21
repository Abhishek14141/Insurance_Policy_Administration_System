package org.example.insurance_policy_system.dto.insurance_policy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDocumentRequestDto {

    @NotNull(message = "Policy ID is required")
    @Positive(message = "Policy ID must be positive")
    private Long policyId;

    @NotBlank(message = "Document name is required")
    private String documentName;

    @NotBlank(message = "File path is required")
    private String filePath;

    @NotNull(message = "Version number is required")
    @Positive(message = "Version number must be positive")
    private Long versionNo;
}