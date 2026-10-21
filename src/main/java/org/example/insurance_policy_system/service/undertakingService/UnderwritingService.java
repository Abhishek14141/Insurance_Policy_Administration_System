package org.example.insurance_policy_system.service.undertakingService;

import org.example.insurance_policy_system.dto.undertakingDto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.entity.undertaking.UnderwritingCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UnderwritingService {

    ApiResponse<UnderwritingResponseDto> createCase(UnderwritingRequestDto requestDto);
//hrr user ki case specific
  ApiResponse<UnderwritingResponseDto> getCaseById(Long caseId);
  //saari customer ki case
    ApiResponse<List<UnderwritingResponseDto>>  getAllCase();
    //medical submit hoga
    ApiResponse<MedicalUnderwritingResponseDto> submitMedicalAssessment(Long caseId, MedicalUnderwritingRequestDto requestDto);
    //finanical submit hoga
    ApiResponse<FinancialUnderwritingResponseDto> submitFinancialAssessment(Long caseId, FinancialUnderwritingRequestDto requestDto);
  ApiResponse<LifestyleUnderwritingResponseDto> submitLifestyleAssessment(Long caseId, LifestyleUnderwritingRequestDto requestDto);

    //calcuate risk Score and catergory
    ApiResponse<RiskAssessmentResponseDto> calculateRisk(Long caseId);
    //calculate premium loading
    ApiResponse<UnderwritingResponseDto> calculatePremiumLoading(Long caseId);
    //Approve case
    ApiResponse<UnderwritingResponseDto> approveCase(Long caseId, UnderwritingDecisionRequestDto requestDto);
    //hold pending document or medical examination
    ApiResponse<UnderwritingResponseDto> markPending(Long caseId,UnderwritingDecisionRequestDto requestDto);
    //apply exclusion case
    ApiResponse<UnderwritingResponseDto> applyExclusions(Long caseId,UnderwritingDecisionRequestDto requestDto);
    //senoir officer approve karega
    ApiResponse<UnderwritingResponseDto> seniorApproveCase(Long caseId,UnderwritingDecisionRequestDto requestDto);
    //aduit history
     ApiResponse<List<AuditResponseDto>> getAuditHistory(long CaseId);

}
