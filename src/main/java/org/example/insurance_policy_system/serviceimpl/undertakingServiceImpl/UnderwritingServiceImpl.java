package org.example.insurance_policy_system.serviceimpl.undertakingServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.configuration.QuoteClient;
import org.example.insurance_policy_system.dto.undertakingDto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.entity.undertaking.FinancialUnderwriting;
import org.example.insurance_policy_system.entity.undertaking.MedicalUnderwriting;
import org.example.insurance_policy_system.entity.undertaking.UnderwritingCase;
import org.example.insurance_policy_system.enums.UnderwritingStatus;
import org.example.insurance_policy_system.exception.StatusNotFound;
import org.example.insurance_policy_system.exception.undertakingException.CaseIdNotFound;
import org.example.insurance_policy_system.exception.undertakingException.QoteIsExpriy;
import org.example.insurance_policy_system.exception.undertakingException.duplicateCaseId;
import org.example.insurance_policy_system.repository.undertakingRepository.FinancialUnderwritingRepository;
import org.example.insurance_policy_system.repository.undertakingRepository.MedicalUnderwritingRepository;
import org.example.insurance_policy_system.repository.undertakingRepository.UnderwritingCaseRepository;
import org.example.insurance_policy_system.service.undertakingService.UnderwritingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnderwritingServiceImpl implements UnderwritingService {
    private final UnderwritingCaseRepository underwritingCaseRepository;
    private final QuoteClient quoteClient;
    private final ModelMapper modelMapper;
    private final MedicalUnderwritingRepository medicalUnderwritingRepository;
    private final FinancialUnderwritingRepository financialUnderwritingRepository;

    @Override
    public ApiResponse<UnderwritingResponseDto> createCase(UnderwritingRequestDto requestDto) {
        log.info("create underwriting case request for quoteId:{}", requestDto.getQuoteId());
        QuoteResponse quote = quoteClient.getQuoteById(requestDto.getQuoteId());
        log.info("fetching details from quote-service using Fenclient for  quoteId:{}", requestDto.getQuoteId());

        if (!"APPROVED".equalsIgnoreCase(quote.getQuoteStatus())) {
            log.warn("underwritinmg case was not created beacusde the  not approved by quoteId:{}", requestDto.getQuoteId());
            throw new StatusNotFound("Underwriting case can be created only for an approved quote quoteId:{}");
        }

            if (quote.getQuoteExpiryDate() == null || quote.getQuoteExpiryDate().isBefore(LocalDate.now())) {
                log.warn("Underwriting case creation rejected. Quote expired. quoteId: {}", requestDto.getQuoteId());
                throw new QoteIsExpriy("Cannot create underwriting case for an expired quote");
            }
            if (underwritingCaseRepository.existsByQuoteId(requestDto.getQuoteId())) {
                log.warn("Underwriting case already exists for quoteId: {}", requestDto.getQuoteId());
                throw new duplicateCaseId("Underwriting case already exists for this quote");
            }
            //covert quote into entity
            UnderwritingCase underwritingCase = modelMapper.map(quote, UnderwritingCase.class);
            underwritingCase.setUnderwritingDate(LocalDate.now());
            underwritingCase.setStatus(UnderwritingStatus.PENDING);
            //save case
            UnderwritingCase savedCase = underwritingCaseRepository.save(underwritingCase);
            log.info("Underwriting case created successfully. caseId: {}, quoteId: {}",
                    savedCase.getUnderwritingId(), savedCase.getQuoteId());

            UnderwritingResponseDto responseDto = modelMapper.map(savedCase, UnderwritingResponseDto.class);
            return new ApiResponse<>(true, "Underwriting case created successfully", responseDto, LocalDateTime.now());



    }

    @Override
    public ApiResponse<UnderwritingResponseDto> getCaseById(Long caseId) {
        log.info("fetching caseId by repository  with ID: {}", caseId);
        UnderwritingCase underwritingCase=underwritingCaseRepository.findById(caseId).orElseThrow(()->{
                log.warn("case id not found:{}",caseId);
                return new CaseIdNotFound("Underwriting case not found with ID: " + caseId);});
        UnderwritingResponseDto responseDto=modelMapper.map(underwritingCase, UnderwritingResponseDto.class);
        log.info("Underwriting case fetched successfully: {}", caseId);
        return new ApiResponse<>(true,  "Underwriting case fetched successfully",
                responseDto,LocalDateTime.now());
    }

    @Override
    public ApiResponse<List<UnderwritingResponseDto>> getAllCase(){
        log.info("fetching all underwriting case");
       List<UnderwritingCase> cases=underwritingCaseRepository.findAll();
        log.info("fetched all underwriting cases");
        List<UnderwritingResponseDto> responseDtoList=cases.stream().map(
                underwritingCase ->
                modelMapper.map(underwritingCase, UnderwritingResponseDto.class)).toList();
        return new ApiResponse<>(true,"Underwriting case fetched successfully", responseDtoList,LocalDateTime.now());

    }

    @Override
    public ApiResponse<UnderwritingResponseDto> submitMedicalAssessment(Long caseId, MedicalUnderwritingRequestDto requestDto) {
        log.info("Submitting medical assessment for caseId: {}", caseId);
        UnderwritingCase underwritingCase =
                underwritingCaseRepository.findById(caseId).orElseThrow(() -> {
                            log.warn("Case ID not found: {}", caseId);
                            return new CaseIdNotFound("Underwriting case not found with ID: " + caseId);});

        //covert into request ko entity ke data mein
            MedicalUnderwriting medicalUnderwriting = modelMapper.map(requestDto, MedicalUnderwriting.class);
            medicalUnderwriting.setMedicalId(caseId);
            medicalUnderwritingRepository.save(medicalUnderwriting);
            UnderwritingResponseDto underwritingResponseDto = modelMapper.map(underwritingCase, UnderwritingResponseDto.class);
            log.info("Medical assessment saved for caseId: {}", caseId);
            return new ApiResponse<>(true, "Medical assessment submitted successfully", underwritingResponseDto, LocalDateTime.now());



    }

    @Override
    public ApiResponse<UnderwritingResponseDto> submitFinancialAssessment(Long caseId, FinancialUnderwritingRequestDto requestDto) {
        log.info("Submitting financial assessment for caseId: {}", caseId);
        UnderwritingCase underwritingCase=underwritingCaseRepository.findById(caseId).orElseThrow(()->{
                log.warn("Case ID not found: {}", caseId);
        return new CaseIdNotFound("Underwriting case not found with ID: " + caseId);
    } );
        FinancialUnderwriting financialUnderwriting=modelMapper.map(requestDto, FinancialUnderwriting.class);
        financialUnderwriting.setFinancialId(caseId);
        financialUnderwritingRepository.save(financialUnderwriting);
        UnderwritingResponseDto responseDto=modelMapper.map(underwritingCase, UnderwritingResponseDto.class);
        log.info("financial assessment saved for caseId: {}", caseId);
        return new ApiResponse<>(true, " assessment financial successfully", responseDto, LocalDateTime.now());

    }

    @Override
    public ApiResponse<UnderwritingResponseDto> submitLifestyleAssessment(Long caseId, LifestyleUnderwritingRequestDto requestDto) {
        log.info("Submitting lifeStyle assessment for caseId: {}", caseId);
        UnderwritingCase underwritingCase=underwritingCaseRepository.findById(caseId).orElseThrow(()->{
            log.warn("Case ID not found: {}", caseId);
            return new CaseIdNotFound("Underwriting case not found with ID: " + caseId);
        } );
        MedicalUnderwriting medicalUnderwriting = medicalUnderwritingRepository.findFirstByUnderwritingCase(underwritingCase)
                        .orElseThrow(() -> new IllegalStateException("Submit medical assessment before lifestyle assessment"));

        medicalUnderwriting.setSmoking(requestDto.getSmoking());
        medicalUnderwriting.setAlcohol(requestDto.getAlcoholConsumption());
        medicalUnderwriting.setAdventureSports(requestDto.getAdventureSports());
        medicalUnderwriting.setFrequentInternationalTravel(
                requestDto.getFrequentInternationalTravel());
        medicalUnderwriting.setDangerousHobbies(requestDto.getDangerousHobbies());
        medicalUnderwritingRepository.save(medicalUnderwriting);
        UnderwritingResponseDto responseDto = modelMapper.map(underwritingCase, UnderwritingResponseDto.class);
        log.info("Lifestyle assessment saved for caseId: {}", caseId);
        return new ApiResponse<>(true, "Lifestyle assessment submitted successfully", responseDto, LocalDateTime.now());

    }

    @Override
    public ApiResponse<UnderwritingResponseDto> calculateRisk(Long caseId) {
        return null;
    }

    @Override
    public ApiResponse<UnderwritingResponseDto> calculatePremiumLoading(Long caseId) {
        return null;
    }

    @Override
    public ApiResponse<UnderwritingResponseDto> approveCase(Long caseId, UnderwritingDecisionRequestDto requestDto) {
        return null;
    }

    @Override
    public ApiResponse<UnderwritingResponseDto> markPending(Long caseId, UnderwritingDecisionRequestDto requestDto) {
        return null;
    }

    @Override
    public ApiResponse<UnderwritingResponseDto> applyExclusions(Long caseId, UnderwritingDecisionRequestDto requestDto) {
        return null;
    }

    @Override
    public ApiResponse<UnderwritingResponseDto> seniorApproveCase(Long caseId, UnderwritingDecisionRequestDto requestDto) {
        return null;
    }

    @Override
    public ApiResponse<List<AuditResponseDto>> getAuditHistory(long CaseId) {
        return null;
    }

}