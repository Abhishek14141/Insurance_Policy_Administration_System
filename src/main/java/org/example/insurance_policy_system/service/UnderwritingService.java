package org.example.insurance_policy_system.service;

import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface UnderwritingService {
    ApiResponse<UnderwritingResponseDto> createCase(UnderwritingRequestDto request);
    ApiResponse<UnderwritingResponseDto> getCase(Long caseId);
    ApiResponse<List<UnderwritingResponseDto>> getAllCases();
    ApiResponse<List<UnderwritingResponseDto>> getPendingCases();
    ApiResponse<UnderwritingResponseDto> updateCase(Long caseId, UnderwritingRequestDto request);
    ApiResponse<MedicalUnderwritingResponseDto> submitMedical(Long caseId, MedicalUnderwritingRequestDto request);
    ApiResponse<MedicalUnderwritingResponseDto> getMedical(Long caseId);
    ApiResponse<MedicalUnderwritingResponseDto> updateMedical(Long caseId, MedicalUnderwritingRequestDto request);
    ApiResponse<MedicalUnderwritingResponseDto> uploadMedicalReport(Long caseId, MultipartFile file);
    Resource viewMedicalReport(Long caseId);
    ApiResponse<FinancialUnderwritingResponseDto> submitFinancial(Long caseId, FinancialUnderwritingRequestDto request);
    ApiResponse<FinancialUnderwritingResponseDto> getFinancial(Long caseId);
    ApiResponse<FinancialUnderwritingResponseDto> updateFinancial(Long caseId, FinancialUnderwritingRequestDto request);
    ApiResponse<LifestyleUnderwritingResponseDto> submitLifestyle(Long caseId, LifestyleUnderwritingRequestDto request);
    ApiResponse<RiskAssessmentResponseDto> calculateRisk(Long caseId);
    ApiResponse<RiskAssessmentResponseDto> getRisk(Long caseId);
    ApiResponse<UnderwritingDecisionResponseDto> approve(Long caseId, UnderwritingDecisionRequestDto request);
    ApiResponse<UnderwritingDecisionResponseDto> reject(Long caseId, UnderwritingDecisionRequestDto request);
    ApiResponse<UnderwritingDecisionResponseDto> applyLoading(Long caseId, java.math.BigDecimal loading, UnderwritingDecisionRequestDto request);
    ApiResponse<UnderwritingDecisionResponseDto> getDecision(Long caseId);
    ApiResponse<List<AuditResponseDto>> getAudit(Long caseId);
    PolicyUnderwritingDetailsDto getPolicyDetailsByQuoteId(Long quoteId);
}
