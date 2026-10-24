package org.example.insurance_policy_system.dto.insurance_policy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDocumentResponseDto {

    private Long documentId;
    private Long policyId;
    private String documentName;
    private String filePath;
    private Long versionNo;
    private LocalDateTime generatedDate;
}