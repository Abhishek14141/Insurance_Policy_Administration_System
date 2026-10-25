
package org.example.insurance_policy_system.controller.kyc;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.example.insurance_policy_system.dto.kyc.*;
import org.example.insurance_policy_system.entity.kyc.*;
import org.example.insurance_policy_system.serviceimpl.kyc.KycService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/kyc")
public class KycController {

    private final KycService service;

    public KycController(KycService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<KycRequest> create(
            @Valid @RequestBody KycCreateRequest in) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(in));
    }

    @GetMapping
    public List<KycRequest> all(
            @RequestParam(required = false) String status) {

        return status == null ? service.all() : service.byStatus(status);
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return service.dashboard();
    }

    @GetMapping("/{kycId}")
    public KycRequest byId(@PathVariable Long kycId) {
        return service.byId(kycId);
    }

    @PutMapping("/{kycId}/identity")
    public KycRequest updateIdentity(
            @PathVariable Long kycId,
            @Valid @RequestBody KycIdentityRequest in) {

        return service.updateIdentity(kycId, in);
    }

    @PutMapping("/{kycId}")
    public KycRequest update(
            @PathVariable Long kycId,
            @Valid @RequestBody KycCreateRequest in) {

        return service.update(kycId, in);
    }

    @DeleteMapping("/{kycId}")
    public ResponseEntity<Void> delete(@PathVariable Long kycId) {
        service.delete(kycId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KycDocument> upload(
            @RequestParam Long kycId,
            @RequestParam String documentType,
            @RequestParam String documentNumber,
            @RequestPart("file") MultipartFile file) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.upload(
                        kycId, documentType, documentNumber, file));
    }

    @GetMapping("/{kycId}/documents")
    public List<KycDocument> docs(@PathVariable Long kycId) {
        return service.docs(kycId);
    }

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<InputStreamResource> view(
            @PathVariable Long documentId) throws IOException {

        KycDocument document = service.document(documentId);
        Path path = Paths.get(document.getFilePath());

        if (!Files.exists(path)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Stored document file not found");
        }

        String type = Files.probeContentType(path);
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        if (type != null) {
            try {
                mediaType = MediaType.parseMediaType(type);
            } catch (Exception ignored) {
                // Use the default media type.
            }
        }

        String fileName = document.getFileName().replace("\"", "");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(Files.newInputStream(path)));
    }

    @PutMapping("/documents/{documentId}/verify")
    public KycDocument verifyDocument(
            @PathVariable Long documentId,
            @RequestParam(required = false) Long performedBy,
            @RequestParam(required = false) String remarks) {

        return service.documentDecision(
                documentId, true, performedBy, remarks);
    }

    @PutMapping("/documents/{documentId}/reject")
    public KycDocument rejectDocument(
            @PathVariable Long documentId,
            @RequestParam(required = false) Long performedBy,
            @RequestParam String reason) {

        return service.documentDecision(
                documentId, false, performedBy, reason);
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long documentId) {

        service.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ckyc/verify")
    public KycRequest verifyCkyc(
            @RequestParam Long customerId,
            @RequestParam(required = false) String ckycNumber,
            @RequestParam(defaultValue = "false") boolean found) {

        return service.ckyc(customerId, ckycNumber, found);
    }

    @GetMapping("/ckyc/{customerId}")
    public Map<String, Object> ckyc(@PathVariable Long customerId) {

        KycRequest kyc = service.all().stream()
                .filter(k -> k.getCustomer()
                        .getCustomerId().equals(customerId))
                .max(Comparator.comparing(KycRequest::getId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "KYC not found"));

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("customerId", customerId);
        response.put("ckycNumber", kyc.getCkycNumber());
        response.put("ckycStatus", kyc.getCkycStatus());
        response.put("verificationDate", kyc.getCkycVerificationDate());

        return response;
    }

    @PutMapping("/{kycId}/approve")
    public KycRequest approve(
            @PathVariable Long kycId,
            @RequestParam(required = false) Long performedBy,
            @RequestParam(required = false) String remarks) {

        return service.approve(kycId, performedBy, remarks);
    }

    @PutMapping("/{kycId}/reject")
    public KycRequest reject(
            @PathVariable Long kycId,
            @Valid @RequestBody KycDecisionRequest in) {

        return service.reject(kycId, in.performedBy(), in.reason());
    }

    @PutMapping("/{kycId}/resubmit")
    public KycRequest resubmit(
            @PathVariable Long kycId,
            @RequestParam(required = false) Long performedBy,
            @RequestParam(required = false) String remarks) {

        return service.resubmit(kycId, performedBy, remarks);
    }

    @GetMapping("/{kycId}/audit")
    public List<KycAuditLog> audit(@PathVariable Long kycId) {
        return service.audit(kycId);
    }
}
