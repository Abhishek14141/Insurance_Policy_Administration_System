package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.UnderwritingService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/underwriting")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class UnderwritingController {
    private final UnderwritingService underwritingService;

    @PostMapping
    public ResponseEntity<ApiResponse<UnderwritingResponseDto>> createCase(@Valid @RequestBody UnderwritingRequestDto request) {
        log.info("POST create underwriting case quoteId={}", request.getQuoteId());
        return ResponseEntity.ok(underwritingService.createCase(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UnderwritingResponseDto>>> getAllCases() {
        log.info("GET all underwriting cases");
        return ResponseEntity.ok(underwritingService.getAllCases());
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<UnderwritingResponseDto>>> getPendingCases() {
        log.info("GET pending underwriting cases");
        return ResponseEntity.ok(underwritingService.getPendingCases());
    }

    @GetMapping("/{caseId}")
    public ResponseEntity<ApiResponse<UnderwritingResponseDto>> getCase(@PathVariable Long caseId) {
        log.info("GET underwriting caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.getCase(caseId));
    }

    @PutMapping("/{caseId}")
    public ResponseEntity<ApiResponse<UnderwritingResponseDto>> updateCase(@PathVariable Long caseId, @Valid @RequestBody UnderwritingRequestDto request) {
        log.info("PUT underwriting caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.updateCase(caseId, request));
    }

    @PostMapping("/{caseId}/medical")
    public ResponseEntity<ApiResponse<MedicalUnderwritingResponseDto>> submitMedical(@PathVariable Long caseId, @Valid @RequestBody MedicalUnderwritingRequestDto request) {
        log.info("POST medical assessment caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.submitMedical(caseId, request));
    }

    @GetMapping("/{caseId}/medical")
    public ResponseEntity<ApiResponse<MedicalUnderwritingResponseDto>> getMedical(@PathVariable Long caseId) {
        log.info("GET medical assessment caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.getMedical(caseId));
    }

    @PutMapping("/{caseId}/medical")
    public ResponseEntity<ApiResponse<MedicalUnderwritingResponseDto>> updateMedical(@PathVariable Long caseId, @Valid @RequestBody MedicalUnderwritingRequestDto request) {
        log.info("PUT medical assessment caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.updateMedical(caseId, request));
    }

    @PostMapping(value = "/{caseId}/medical-report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MedicalUnderwritingResponseDto>> uploadMedicalReport(@PathVariable Long caseId, @RequestPart("medicalReport") MultipartFile medicalReport) {
        log.info("POST medical report upload caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.uploadMedicalReport(caseId, medicalReport));
    }

    @GetMapping("/{caseId}/medical-report")
    public ResponseEntity<Resource> viewMedicalReport(@PathVariable Long caseId) {
        log.info("GET medical report caseId={}", caseId);
        Resource resource = underwritingService.viewMedicalReport(caseId);
        MediaType contentType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = resource.getFilename() == null ? "medical-report" : resource.getFilename();
        return ResponseEntity.ok().contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PostMapping("/{caseId}/financial")
    public ResponseEntity<ApiResponse<FinancialUnderwritingResponseDto>> submitFinancial(@PathVariable Long caseId, @Valid @RequestBody FinancialUnderwritingRequestDto request) {
        log.info("POST financial assessment caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.submitFinancial(caseId, request));
    }

    @GetMapping("/{caseId}/financial")
    public ResponseEntity<ApiResponse<FinancialUnderwritingResponseDto>> getFinancial(@PathVariable Long caseId) {
        log.info("GET financial assessment caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.getFinancial(caseId));
    }

    @PutMapping("/{caseId}/financial")
    public ResponseEntity<ApiResponse<FinancialUnderwritingResponseDto>> updateFinancial(@PathVariable Long caseId, @Valid @RequestBody FinancialUnderwritingRequestDto request) {
        log.info("PUT financial assessment caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.updateFinancial(caseId, request));
    }

    @PostMapping("/{caseId}/lifestyle")
    public ResponseEntity<ApiResponse<LifestyleUnderwritingResponseDto>> submitLifestyle(@PathVariable Long caseId, @RequestBody LifestyleUnderwritingRequestDto request) {
        log.info("POST lifestyle assessment caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.submitLifestyle(caseId, request));
    }

    @PostMapping("/{caseId}/risk-assessment")
    public ResponseEntity<ApiResponse<RiskAssessmentResponseDto>> calculateRisk(@PathVariable Long caseId) {
        log.info("POST calculate risk caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.calculateRisk(caseId));
    }

    @GetMapping("/{caseId}/risk-assessment")
    public ResponseEntity<ApiResponse<RiskAssessmentResponseDto>> getRisk(@PathVariable Long caseId) {
        log.info("GET risk assessment caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.getRisk(caseId));
    }

    @PutMapping("/{caseId}/approve")
    public ResponseEntity<ApiResponse<UnderwritingDecisionResponseDto>> approve(@PathVariable Long caseId, @Valid @RequestBody UnderwritingDecisionRequestDto request) {
        log.info("PUT approve caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.approve(caseId, request));
    }

    @PutMapping("/{caseId}/reject")
    public ResponseEntity<ApiResponse<UnderwritingDecisionResponseDto>> reject(@PathVariable Long caseId, @RequestBody UnderwritingDecisionRequestDto request) {
        log.info("PUT reject caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.reject(caseId, request));
    }

    @PutMapping("/{caseId}/loading")
    public ResponseEntity<ApiResponse<UnderwritingDecisionResponseDto>> applyLoading(@PathVariable Long caseId, @RequestParam BigDecimal loadingPercentage, @RequestBody UnderwritingDecisionRequestDto request) {
        log.info("PUT loading caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.applyLoading(caseId, loadingPercentage, request));
    }

    @GetMapping("/{caseId}/decision")
    public ResponseEntity<ApiResponse<UnderwritingDecisionResponseDto>> getDecision(@PathVariable Long caseId) {
        log.info("GET decision caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.getDecision(caseId));
    }

    @GetMapping("/{caseId}/audit")
    public ResponseEntity<ApiResponse<List<AuditResponseDto>>> getAudit(@PathVariable Long caseId) {
        log.info("GET audit caseId={}", caseId);
        return ResponseEntity.ok(underwritingService.getAudit(caseId));
    }
    @GetMapping("/quote/{quoteId}/policy-details")
    public PolicyUnderwritingDetailsDto getPolicyDetailsByQuoteId(
            @PathVariable Long quoteId) {

        return underwritingService.getPolicyDetailsByQuoteId(quoteId);
    }
}
