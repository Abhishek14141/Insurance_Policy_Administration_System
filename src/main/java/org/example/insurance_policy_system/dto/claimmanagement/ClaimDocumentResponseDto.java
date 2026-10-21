package org.example.insurance_policy_system.dto.claimmanagement;

import lombok.*;
import org.example.insurance_policy_system.enums.claimmanagement.DocumentVerificationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDocumentResponseDto {

    private Long documentId;
    private Long claimId;
    private String documentName;
    private String filePath;
    private DocumentVerificationStatus verificationStatus;
}