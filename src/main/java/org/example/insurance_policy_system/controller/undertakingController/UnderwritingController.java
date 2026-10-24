  package org.example.insurance_policy_system.controller.undertakingController;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.insurance_policy_system.dto.undertakingDto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.undertakingService.UnderwritingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UnderwritingController {

    private final UnderwritingService underwritingService;
    ObjectMapper objectMapper = new ObjectMapper();

    // Create underwriting case
    @PostMapping("/cases")
    public ResponseEntity<ApiResponse<UnderwritingResponseDto>> createCase(
            @Valid @RequestBody UnderwritingRequestDto requestDto) {

        log.info("Creating underwriting case");

        ApiResponse<UnderwritingResponseDto> response =
                underwritingService.createCase(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get case by ID
    @GetMapping("/cases/{caseId}")
    public ResponseEntity<ApiResponse<UnderwritingResponseDto>> getCaseById(
            @PathVariable Long caseId) {

        log.info("Fetching underwriting case: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.getCaseById(caseId));
    }

    // Get all cases
    @GetMapping("/cases")
    public ResponseEntity<ApiResponse<List<UnderwritingResponseDto>>> getAllCases() {
        log.info("Fetching all underwriting cases");
        return ResponseEntity.ok(underwritingService.getAllCase());
    }

    @PostMapping(
            value = "/cases/{caseId}/medical-assessment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<MedicalUnderwritingResponseDto>>
    submitMedicalAssessment(
            @PathVariable Long caseId,
            @RequestParam("requestDto") String requestJson,
            @RequestPart(value = "medicalReport", required = false)
            MultipartFile medicalReport) throws Exception {

        MedicalUnderwritingRequestDto requestDto =
                objectMapper.readValue(
                        requestJson,
                        MedicalUnderwritingRequestDto.class
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(underwritingService.submitMedicalAssessment(
                        caseId, requestDto, medicalReport));
    }
    // Get medical assessment
    @GetMapping("/cases/{caseId}/medical-assessment")
    public ResponseEntity<ApiResponse<MedicalUnderwritingResponseDto>>
    getMedicalAssessment(@PathVariable Long caseId) {

        log.info("Fetching medical assessment for caseId: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.getMedicalAssessment(caseId));
    }

    // Update medical assessment
    @PutMapping(
            value = "/cases/{caseId}/medical-assessment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<MedicalUnderwritingResponseDto>>
    updateMedicalAssessment(
            @PathVariable Long caseId,
            @RequestParam("requestDto") String requestJson,
            @RequestParam(value = "medicalReport", required = false)
            MultipartFile medicalReport) throws JsonProcessingException {

        log.info("Updating medical assessment for caseId: {}", caseId);

        MedicalUnderwritingRequestDto requestDto =
                objectMapper.readValue(
                        requestJson,
                        MedicalUnderwritingRequestDto.class
                );

        return ResponseEntity.ok(
                underwritingService.updateMedicalAssessment(
                        caseId, requestDto, medicalReport
                )
        );
    }

    // Submit financial assessment
    @PostMapping("/cases/{caseId}/financial-assessment")
    public ResponseEntity<ApiResponse<FinancialUnderwritingResponseDto>>
    submitFinancialAssessment(
            @PathVariable Long caseId,
            @Valid @RequestBody FinancialUnderwritingRequestDto requestDto) {

        log.info("Submitting financial assessment for caseId: {}", caseId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                underwritingService.submitFinancialAssessment(caseId, requestDto));
    }

    // Get financial assessment
    @GetMapping("/cases/{caseId}/financial-assessment")
    public ResponseEntity<ApiResponse<FinancialUnderwritingResponseDto>>
    getFinancialAssessment(@PathVariable Long caseId) {

        log.info("Fetching financial assessment for caseId: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.getFinancialAssessment(caseId));
    }

    // Update financial assessment
    @PutMapping("/cases/{caseId}/financial-assessment")
    public ResponseEntity<ApiResponse<FinancialUnderwritingResponseDto>>
    updateFinancialAssessment(
            @PathVariable Long caseId,
            @Valid @RequestBody FinancialUnderwritingRequestDto requestDto) {

        log.info("Updating financial assessment for caseId: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.updateFinancialAssessment(caseId, requestDto));
    }

    // Submit lifestyle assessment
    @PostMapping("/cases/{caseId}/lifestyle-assessment")
    public ResponseEntity<ApiResponse<LifestyleUnderwritingResponseDto>>
    submitLifestyleAssessment(
            @PathVariable Long caseId,
            @Valid @RequestBody LifestyleUnderwritingRequestDto requestDto) {

        log.info("Submitting lifestyle assessment for caseId: {}", caseId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                underwritingService.submitLifestyleAssessment(caseId, requestDto));
    }

    // Calculate risk
    @PostMapping("/cases/{caseId}/calculate-risk")
    public ResponseEntity<ApiResponse<RiskAssessmentResponseDto>> calculateRisk(
            @PathVariable Long caseId) {

        log.info("Calculating risk for caseId: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.calculateRisk(caseId));
    }

    // Calculate premium loading
    @PostMapping("/cases/{caseId}/premium-loading")
    public ResponseEntity<ApiResponse<UnderwritingDecisionResponseDto>>
    calculatePremiumLoading(@PathVariable Long caseId) {

        log.info("Calculating premium loading for caseId: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.calculatePremiumLoading(caseId));
    }

    // Approve case
    @PutMapping("/cases/{caseId}/approve")
    public ResponseEntity<ApiResponse<UnderwritingDecisionResponseDto>> approveCase(
            @PathVariable Long caseId,
            @Valid @RequestBody UnderwritingDecisionRequestDto requestDto) {

        log.info("Approving underwriting case: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.approveCase(caseId, requestDto));
    }

    // Reject case
    @PutMapping("/cases/{caseId}/reject")
    public ResponseEntity<ApiResponse<UnderwritingDecisionResponseDto>> rejectCase(
            @PathVariable Long caseId,
            @Valid @RequestBody UnderwritingDecisionRequestDto requestDto) {

        log.info("Rejecting underwriting case: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.rejectCase(caseId, requestDto));
    }

    // Get decision
    @GetMapping("/cases/{caseId}/decision")
    public ResponseEntity<ApiResponse<UnderwritingDecisionResponseDto>> getDecision(
            @PathVariable Long caseId) {

        log.info("Fetching decision for caseId: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.getDecision(caseId));
    }

    // Get audit history
    @GetMapping("/cases/{caseId}/audit-history")
    public ResponseEntity<ApiResponse<List<AuditResponseDto>>> getAuditHistory(
            @PathVariable long caseId) {

        log.info("Fetching audit history for caseId: {}", caseId);

        return ResponseEntity.ok(
                underwritingService.getAuditHistory(caseId));
    }

    // Get pending cases for officer
    @GetMapping("/cases/pending")
    public ResponseEntity<ApiResponse<List<UnderwritingResponseDto>>> getPendingCases() {

        log.info("Fetching pending underwriting cases");

        return ResponseEntity.ok(
                underwritingService.getPendingCases());
    }
}
