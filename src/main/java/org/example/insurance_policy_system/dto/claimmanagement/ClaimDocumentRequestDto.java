package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDocumentRequestDto {

    @NotBlank
    private String documentName;

    @NotNull
    private MultipartFile file;
}