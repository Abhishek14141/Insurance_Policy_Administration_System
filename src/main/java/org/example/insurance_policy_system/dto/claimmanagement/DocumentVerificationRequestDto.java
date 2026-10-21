package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.insurance_policy_system.enums.claimmanagement.DocumentVerificationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVerificationRequestDto {

    @NotNull
    private Long documentId;

    @NotNull
    private DocumentVerificationStatus verificationStatus;
}