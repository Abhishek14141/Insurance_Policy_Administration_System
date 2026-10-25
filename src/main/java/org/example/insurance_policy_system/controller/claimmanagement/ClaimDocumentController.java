
package org.example.insurance_policy_system.controller.claimmanagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.insurance_policy_system.dto.claimmanagement.ClaimDocumentRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimDocumentResponseDto;
import org.example.insurance_policy_system.dto.claimmanagement.DocumentVerificationRequestDto;
import org.example.insurance_policy_system.service.claimmanagement.ClaimDocumentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class ClaimDocumentController {

    private final ClaimDocumentService documentService;

    // 1. Upload document
    @PostMapping(
            value = "/{claimId}/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ClaimDocumentResponseDto> uploadDocument(
            @PathVariable("claimId") Long claimId,
            @Valid @ModelAttribute ClaimDocumentRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentService.uploadDocument(claimId, requestDto));
    }

    // 2. Get claim documents
    @GetMapping("/{claimId}/documents")
    public ResponseEntity<List<ClaimDocumentResponseDto>> getDocuments(
            @PathVariable("claimId") Long claimId) {

        return ResponseEntity.ok(
                documentService.getClaimDocuments(claimId)
        );
    }

    // 3. Verify or reject document
    @PatchMapping("/{claimId}/documents/verify")
    public ResponseEntity<ClaimDocumentResponseDto> verifyDocument(
            @PathVariable("claimId") Long claimId,
            @Valid @RequestBody DocumentVerificationRequestDto requestDto) {

        return ResponseEntity.ok(
                documentService.verifyDocument(claimId, requestDto)
        );
    }
}
