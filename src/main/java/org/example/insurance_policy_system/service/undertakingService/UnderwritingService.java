package org.example.insurance_policy_system.service.undertakingService;

import org.example.insurance_policy_system.dto.undertakingDto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.entity.undertaking.UnderwritingCase;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UnderwritingService {

    ApiResponse<UnderwritingResponseDto> createCase(UnderwritingRequestDto requestDto);
//hrr user ki case specific
  ApiResponse<UnderwritingResponseDto> getCaseById(Long caseId);

  //saari customer ki case
    ApiResponse<List<UnderwritingResponseDto>>  getAllCase();
    //medical submit hoga
    ApiResponse<MedicalUnderwritingResponseDto> submitMedicalAssessment(Long caseId, MedicalUnderwritingRequestDto requestDto, MultipartFile medicalReport);
    ApiResponse<MedicalUnderwritingResponseDto> getMedicalAssessment(Long caseId);

  ApiResponse<MedicalUnderwritingResponseDto> updateMedicalAssessment(
          Long caseId, MedicalUnderwritingRequestDto requestDto,MultipartFile medicalReport);

    //finanical submit hoga
    ApiResponse<FinancialUnderwritingResponseDto> submitFinancialAssessment(Long caseId, FinancialUnderwritingRequestDto requestDto);
  ApiResponse<LifestyleUnderwritingResponseDto> submitLifestyleAssessment(Long caseId, LifestyleUnderwritingRequestDto requestDto);
  ApiResponse<FinancialUnderwritingResponseDto> getFinancialAssessment(
          Long caseId);

  ApiResponse<FinancialUnderwritingResponseDto> updateFinancialAssessment(
          Long caseId, FinancialUnderwritingRequestDto requestDto);


    //calcuate risk Score and catergory
    ApiResponse<RiskAssessmentResponseDto> calculateRisk(Long caseId);
    //calculate premium loading
    ApiResponse<UnderwritingDecisionResponseDto> calculatePremiumLoading(Long caseId);
    //Approve case
    ApiResponse<UnderwritingDecisionResponseDto> approveCase(Long caseId, UnderwritingDecisionRequestDto requestDto);
  ApiResponse<List<UnderwritingResponseDto>> getPendingCases();
  ApiResponse<UnderwritingDecisionResponseDto> rejectCase(Long caseId, UnderwritingDecisionRequestDto requestDto);
  ApiResponse<UnderwritingDecisionResponseDto> getDecision(Long caseId);


  //aduit history
     ApiResponse<List<AuditResponseDto>> getAuditHistory(long caseId);

}
