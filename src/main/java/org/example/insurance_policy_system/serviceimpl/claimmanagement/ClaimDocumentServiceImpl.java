package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimDocumentRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimDocumentResponseDto;
import org.example.insurance_policy_system.dto.claimmanagement.DocumentVerificationRequestDto;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimDocuments;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimMaster;
import org.example.insurance_policy_system.enums.claimmanagement.DocumentVerificationStatus;
import org.example.insurance_policy_system.repository.claimmanagement.ClaimDocumentsRepository;
import org.example.insurance_policy_system.repository.claimmanagement.ClaimMasterRepository;
import org.example.insurance_policy_system.service.claimmanagement.ClaimDocumentService;
import org.example.insurance_policy_system.service.claimmanagement.ClaimHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClaimDocumentServiceImpl implements ClaimDocumentService {


    private static final Long TEST_EMPLOYEE_ID = 1L;

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

    private final ClaimDocumentsRepository documentRepository;
    private final ClaimMasterRepository claimRepository;
    private final ClaimHistoryService historyService;

    private final Path uploadDirectory = Paths.get(
            "uploads", "claim-documents"
    ).toAbsolutePath().normalize();

    // ==========================================
    // 1. UPLOAD DOCUMENT
    // ==========================================

    @Override
    @Transactional
    public ClaimDocumentResponseDto uploadDocument(
            Long claimId,
            ClaimDocumentRequestDto requestDto) {

        // Step 1: Fetch ClaimMaster entity
        ClaimMaster claim = findClaim(claimId);

        // Step 2: Validate request
        if (requestDto == null
                || requestDto.getDocumentName() == null
                || requestDto.getDocumentName().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Document name is required"
            );
        }

        MultipartFile file = requestDto.getFile();

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please select a document"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Maximum file size is 5 MB"
            );
        }

        // Step 3: Validate file type
        String contentType = file.getContentType();

        if (!List.of(
                "application/pdf",
                "image/jpeg",
                "image/png"
        ).contains(contentType)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only PDF, JPG and PNG files are allowed"
            );
        }

        Path targetPath = null;

        try {

            // Step 4: Create directory
            Files.createDirectories(uploadDirectory);

            // Step 5: Determine extension
            String extension = switch (contentType) {
                case "application/pdf" -> ".pdf";
                case "image/jpeg" -> ".jpg";
                case "image/png" -> ".png";
                default -> throw new IllegalStateException(
                        "Unsupported file type"
                );
            };

            // Step 6: Generate unique filename
            String uniqueFileName =
                    UUID.randomUUID() + extension;

            targetPath = uploadDirectory
                    .resolve(uniqueFileName)
                    .normalize();

            // Step 7: Store actual file
            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Step 8: Create document entity
            ClaimDocuments document = new ClaimDocuments();

            // ManyToOne relationship:
            // Pass ClaimMaster, NOT Long claimId.
            document.setClaim(claim);

            document.setDocumentName(
                    requestDto.getDocumentName().trim()
            );

            document.setFilePath(
                    targetPath.toString()
            );

            document.setVerificationStatus(
                    DocumentVerificationStatus.PENDING
            );

            // Step 9: Save document metadata
            ClaimDocuments saved =
                    documentRepository.saveAndFlush(document);

            // Step 10: Maintain history
            historyService.recordHistory(
                    claimId,
                    "DOCUMENT_UPLOADED",
                    TEST_EMPLOYEE_ID,
                    "Uploaded document: " + saved.getDocumentName()
            );

            log.info(
                    "Document uploaded: documentId={}, claimId={}",
                    saved.getDocumentId(),
                    claimId
            );

            // Step 11: Return response
            return toResponse(saved);

        } catch (IOException ex) {

            log.error(
                    "Failed to upload document for claimId={}",
                    claimId,
                    ex
            );

            // Remove partially written file
            if (targetPath != null) {
                try {
                    Files.deleteIfExists(targetPath);
                } catch (IOException cleanupEx) {
                    log.error(
                            "Unable to clean up uploaded file",
                            cleanupEx
                    );
                }
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to upload document",
                    ex
            );
        }
    }

    // ==========================================
    // 2. GET ALL DOCUMENTS OF CLAIM
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<ClaimDocumentResponseDto> getClaimDocuments(
            Long claimId) {

        // Validate claim exists
        findClaim(claimId);

        // Fetch documents using nested entity property
        List<ClaimDocuments> documents =
                documentRepository.findByClaim_ClaimId(claimId);

        return documents.stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================================
    // 3. VERIFY OR REJECT DOCUMENT
    // ==========================================

    @Override
    @Transactional
    public ClaimDocumentResponseDto verifyDocument(
            Long claimId,
            DocumentVerificationRequestDto requestDto) {

        // Step 1: Validate claim
        findClaim(claimId);

        if (requestDto == null
                || requestDto.getDocumentId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Document ID is required"
            );
        }

        // Step 2: Fetch document
        ClaimDocuments document = documentRepository
                .findById(requestDto.getDocumentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Document not found: "
                                + requestDto.getDocumentId()
                ));

        // Step 3: Ensure document belongs to claim
        if (!claimId.equals(
                document.getClaim().getClaimId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Document does not belong to claim: " + claimId
            );
        }

        // Step 4: Validate verification status
        DocumentVerificationStatus status =
                requestDto.getVerificationStatus();

        if (status != DocumentVerificationStatus.VERIFIED
                && status != DocumentVerificationStatus.REJECTED) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status must be VERIFIED or REJECTED"
            );
        }

        // Step 5: Update verification status
        document.setVerificationStatus(status);

        ClaimDocuments saved =
                documentRepository.saveAndFlush(document);

        // Step 6: Maintain history
        historyService.recordHistory(
                claimId,
                "DOCUMENT_" + status.name(),
                TEST_EMPLOYEE_ID,
                "Document " + saved.getDocumentName()
                        + " marked as " + status.name()
        );

        log.info(
                "Document verified: documentId={}, status={}",
                saved.getDocumentId(),
                status
        );

        return toResponse(saved);
    }

    // ==========================================
    // 4. FIND CLAIM
    // ==========================================

    private ClaimMaster findClaim(Long claimId) {

        if (claimId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Claim ID is required"
            );
        }

        return claimRepository.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Claim not found: " + claimId
                ));
    }

    // ==========================================
    // 5. ENTITY TO RESPONSE DTO
    // ==========================================

    private ClaimDocumentResponseDto toResponse(
            ClaimDocuments document) {

        ClaimDocumentResponseDto response =
                new ClaimDocumentResponseDto();

        response.setDocumentId(
                document.getDocumentId()
        );

        response.setClaimId(
                document.getClaim().getClaimId()
        );

        response.setDocumentName(
                document.getDocumentName()
        );

        response.setFilePath(
                document.getFilePath()
        );

        response.setVerificationStatus(
                document.getVerificationStatus()
        );

        return response;
    }
}
