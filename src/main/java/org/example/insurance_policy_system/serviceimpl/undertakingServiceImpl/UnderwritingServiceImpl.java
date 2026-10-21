package org.example.insurance_policy_system.serviceimpl.undertakingServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.client.CustomerClient;
import org.example.insurance_policy_system.client.QuoteClient;
import org.example.insurance_policy_system.dto.undertakingDto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.entity.undertaking.*;
import org.example.insurance_policy_system.enums.RiskLevel;
import org.example.insurance_policy_system.enums.UnderwritingDecisionStatus;
import org.example.insurance_policy_system.enums.UnderwritingStatus;
import org.example.insurance_policy_system.exception.StatusNotFound;
import org.example.insurance_policy_system.exception.undertakingException.BusinessException;
import org.example.insurance_policy_system.exception.undertakingException.CaseIdNotFound;
import org.example.insurance_policy_system.exception.undertakingException.QoteIsExpriy;
import org.example.insurance_policy_system.exception.undertakingException.duplicateCaseId;
import org.example.insurance_policy_system.repository.undertakingRepository.*;
import org.example.insurance_policy_system.service.undertakingService.UnderwritingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnderwritingServiceImpl implements UnderwritingService {
    private final UnderwritingCaseRepository underwritingCaseRepository;
    private final QuoteClient quoteClient;
    private final ModelMapper modelMapper;
    private final MedicalUnderwritingRepository medicalUnderwritingRepository;
    private final FinancialUnderwritingRepository financialUnderwritingRepository;
    private  final CustomerClient customerClient;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final UnderwritingDecisionRepository underwritingDecisionRepository;
    private final UnderwritingAuditRepository underwritingAuditRepository;

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
    @Transactional
    public ApiResponse<MedicalUnderwritingResponseDto> submitMedicalAssessment(
            Long caseId,
            MedicalUnderwritingRequestDto requestDto,
            MultipartFile medicalReport) {

        log.info("Submitting medical assessment for caseId: {}", caseId);

        // 1. Find underwriting case
        UnderwritingCase underwritingCase =
                underwritingCaseRepository.findById(caseId)
                        .orElseThrow(() -> {
                            log.warn("Case ID not found: {}", caseId);
                            return new CaseIdNotFound(
                                    "Underwriting case not found with ID: " + caseId);
                        });

        // 2. Convert request DTO into entity
        MedicalUnderwriting medicalUnderwriting =
                modelMapper.map(requestDto, MedicalUnderwriting.class);

        // 3. Validate height and weight
        BigDecimal height = requestDto.getHeight();
        BigDecimal weight = requestDto.getWeight();

        if (height != null && height.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Height must be positive");
        }

        if (weight != null && weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Weight must be positive");
        }

        // 4. Calculate BMI (height is in centimeters)
        if (height != null && weight != null) {
            BigDecimal heightInMeters =
                    height.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

            BigDecimal bmi = weight.divide(
                    heightInMeters.multiply(heightInMeters),
                    2,
                    RoundingMode.HALF_UP
            );

            medicalUnderwriting.setBmi(bmi);
        } else {
            medicalUnderwriting.setBmi(null);
        }

        // 5. Set relationship
        medicalUnderwriting.setUnderwritingCase(underwritingCase);

        // 6. Upload medical report if provided
        if (medicalReport != null && !medicalReport.isEmpty()) {
            try {
                String originalFilename = Paths
                        .get(medicalReport.getOriginalFilename() == null
                                ? "medical-report.pdf"
                                : medicalReport.getOriginalFilename())
                        .getFileName()
                        .toString();

                String fileName = UUID.randomUUID() + "_" + originalFilename;

                Path uploadDirectory = Paths.get("uploads", "medical-reports")
                        .toAbsolutePath()
                        .normalize();

                Files.createDirectories(uploadDirectory);

                Path filePath = uploadDirectory.resolve(fileName).normalize();

                if (!filePath.startsWith(uploadDirectory)) {
                    throw new BusinessException("Invalid medical report file name");
                }

                medicalReport.transferTo(filePath);
                medicalUnderwriting.setMedicalReportPath(filePath.toString());

            } catch (IOException e) {
                log.error("Medical report upload failed for caseId: {}", caseId, e);
                throw new BusinessException("Medical report upload failed");
            }
        }

        // 7. Save medical assessment
        medicalUnderwriting =
                medicalUnderwritingRepository.save(medicalUnderwriting);

        // 8. Map response manually to avoid ModelMapper ID conflict
        MedicalUnderwritingResponseDto responseDto =
                new MedicalUnderwritingResponseDto();

        responseDto.setMedicalId(medicalUnderwriting.getMedicalId());

        responseDto.setUnderwritingId(
                medicalUnderwriting.getUnderwritingCase().getUnderwritingId()
        );

        responseDto.setHeight(medicalUnderwriting.getHeight());
        responseDto.setWeight(medicalUnderwriting.getWeight());
        responseDto.setBmi(medicalUnderwriting.getBmi());
        responseDto.setAge(medicalUnderwriting.getAge());
        responseDto.setMedicalHistory(medicalUnderwriting.getMedicalHistory());
        responseDto.setMedicalReportPath(
                medicalUnderwriting.getMedicalReportPath()
        );

        log.info("Medical assessment saved for caseId: {}", caseId);

        return new ApiResponse<>(
                true,
                "Medical assessment submitted successfully",
                responseDto,
                LocalDateTime.now()
        );
    }



    @Override
    public ApiResponse<MedicalUnderwritingResponseDto> getMedicalAssessment(Long caseId) {

        log.info("Fetching medical assessment for caseId: {}", caseId);
        // 2. Check whether case exists
        underwritingCaseRepository.findById(caseId).orElseThrow(() ->
                        new CaseIdNotFound("Underwriting case not found with ID: " + caseId));

        // 3. Fetch medical assessment
        MedicalUnderwriting medical = medicalUnderwritingRepository
                .findByUnderwritingCase_UnderwritingId(caseId)
                .orElseThrow(() -> new BusinessException(
                        "Medical assessment not found for caseId: " + caseId
                ));
        // 4. Convert entity into response DTO
        MedicalUnderwritingResponseDto responseDto =
                new MedicalUnderwritingResponseDto();

        responseDto.setMedicalId(medical.getMedicalId());

        responseDto.setUnderwritingId(
                medical.getUnderwritingCase().getUnderwritingId()
        );

        responseDto.setHeight(medical.getHeight());
        responseDto.setWeight(medical.getWeight());
        responseDto.setBmi(medical.getBmi());
        responseDto.setAge(medical.getAge());
        responseDto.setMedicalHistory(medical.getMedicalHistory());
        responseDto.setMedicalReportPath(medical.getMedicalReportPath());
        log.info("Medical assessment fetched successfully for caseId: {}", caseId);
        return new ApiResponse<>(true, "Medical assessment fetched successfully", responseDto, LocalDateTime.now());
    }

    @Override
    @Transactional
    public ApiResponse<MedicalUnderwritingResponseDto> updateMedicalAssessment(
            Long caseId,
            MedicalUnderwritingRequestDto requestDto,
            MultipartFile medicalReport) {

        log.info("Updating medical assessment for caseId: {}", caseId);

        // 1. Validate case ID
        if (caseId == null || caseId <= 0) {
            throw new BusinessException("Invalid underwriting case ID");
        }

        // 2. Find underwriting case
        UnderwritingCase underwritingCase =
                underwritingCaseRepository.findById(caseId)
                        .orElseThrow(() ->
                                new CaseIdNotFound(
                                        "Underwriting case not found with ID: " + caseId));

        // 3. Find existing medical assessment
        MedicalUnderwriting medical =
                medicalUnderwritingRepository
                        .findByUnderwritingCase_UnderwritingId(caseId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Medical assessment not found for caseId: " + caseId));

        // 4. Validate height
        if (requestDto.getHeight() != null
                && requestDto.getHeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Height must be greater than zero");
        }

        // 5. Validate weight
        if (requestDto.getWeight() != null
                && requestDto.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Weight must be greater than zero");
        }

        // 6. Update only provided fields
        if (requestDto.getHeight() != null) {
            medical.setHeight(requestDto.getHeight());
        }

        if (requestDto.getWeight() != null) {
            medical.setWeight(requestDto.getWeight());
        }

        if (requestDto.getAge() != null) {
            medical.setAge(requestDto.getAge());
        }

        if (requestDto.getMedicalHistory() != null) {
            medical.setMedicalHistory(requestDto.getMedicalHistory());
        }

        // 7. Recalculate BMI
        BigDecimal height = medical.getHeight();
        BigDecimal weight = medical.getWeight();

        if (height != null && weight != null) {
            BigDecimal heightInMeters = height.divide(
                    new BigDecimal("100"), 4, RoundingMode.HALF_UP);

            BigDecimal bmi = weight.divide(
                    heightInMeters.multiply(heightInMeters),
                    2, RoundingMode.HALF_UP);

            medical.setBmi(bmi);
        } else {
            medical.setBmi(null);
        }

        // 8. Upload new report if provided
        if (medicalReport != null && !medicalReport.isEmpty()) {
            try {
                String originalFileName = Paths
                        .get(medicalReport.getOriginalFilename())
                        .getFileName()
                        .toString();

                String fileName = UUID.randomUUID() + "_" + originalFileName;

                Path uploadDirectory = Paths
                        .get("uploads", "medical-reports")
                        .toAbsolutePath()
                        .normalize();

                Files.createDirectories(uploadDirectory);

                Path filePath = uploadDirectory.resolve(fileName).normalize();

                if (!filePath.startsWith(uploadDirectory)) {
                    throw new BusinessException("Invalid medical report file path");
                }

                medicalReport.transferTo(filePath);

                medical.setMedicalReportPath(filePath.toString());

            } catch (IOException e) {
                log.error("Medical report upload failed for caseId: {}", caseId, e);
                throw new BusinessException("Medical report upload failed");
            }
        }

        // 9. Preserve relationship and save
        medical.setUnderwritingCase(underwritingCase);

        MedicalUnderwriting updatedMedical =
                medicalUnderwritingRepository.save(medical);

        // 10. Prepare response
        MedicalUnderwritingResponseDto responseDto =
                new MedicalUnderwritingResponseDto();

        responseDto.setMedicalId(medical.getMedicalId());

        responseDto.setUnderwritingId(
                medical.getUnderwritingCase().getUnderwritingId()
        );

        responseDto.setHeight(medical.getHeight());
        responseDto.setWeight(medical.getWeight());
        responseDto.setBmi(medical.getBmi());
        responseDto.setAge(medical.getAge());
        responseDto.setMedicalHistory(medical.getMedicalHistory());
        responseDto.setMedicalReportPath(medical.getMedicalReportPath());

        log.info("Medical assessment updated successfully for caseId: {}", caseId);

        return new ApiResponse<>(
                true,
                "Medical assessment updated successfully",
                responseDto,
                LocalDateTime.now());
    }

    @Override
    public ApiResponse<FinancialUnderwritingResponseDto> submitFinancialAssessment(
            Long caseId,
            FinancialUnderwritingRequestDto requestDto) {

        log.info("Submitting financial assessment for caseId: {}", caseId);

        UnderwritingCase underwritingCase =
                underwritingCaseRepository.findById(caseId).orElseThrow(() -> {
                            log.warn("Case ID not found: {}", caseId);
                            return new CaseIdNotFound("Underwriting case not found with ID: " + caseId);});

        // 2. Get customerId from underwriting case
        Long customerId = underwritingCase.getCustomerId();
        // 3. Fetch customer details from Customer microservice
        CustomerResponse customer = customerClient.getCustomer(customerId);
        if (customer == null) {
            throw new BusinessException("Customer details not found");
        }
        // 4. Map request DTO to entity
        FinancialUnderwriting financialUnderwriting = modelMapper.map(requestDto, FinancialUnderwriting.class);
        // 5. Set fetched customer details
        financialUnderwriting.setAnnualIncome(customer.getAnnualIncome());
        financialUnderwriting.setOccupation(customer.getOccupation());
        // 6. Link the underwriting case
        financialUnderwriting.setUnderwritingCase(underwritingCase);
        // 7. Save financial assessment
        financialUnderwriting = financialUnderwritingRepository.save(financialUnderwriting);

        // 8. Map saved entity to response DTO
        FinancialUnderwritingResponseDto responseDto =
                new FinancialUnderwritingResponseDto();

        responseDto.setFinancialId(financialUnderwriting.getFinancialId());

        responseDto.setUnderwritingId(
                financialUnderwriting.getUnderwritingCase().getUnderwritingId()
        );

        responseDto.setAnnualIncome(financialUnderwriting.getAnnualIncome());
        responseDto.setMonthlyIncome(financialUnderwriting.getMonthlyIncome());
        responseDto.setEmployerName(financialUnderwriting.getEmployerName());
        responseDto.setOccupation(financialUnderwriting.getOccupation());
        responseDto.setExistingInsurance(financialUnderwriting.getExistingInsurance());
        responseDto.setLoanLiability(financialUnderwriting.getLoanLiability());
        log.info("Financial assessment saved for caseId: {}", caseId);

        return new ApiResponse<>(true, "Financial assessment saved successfully", responseDto, LocalDateTime.now());
    }

    @Override
    public ApiResponse<LifestyleUnderwritingResponseDto> submitLifestyleAssessment(
            Long caseId,
            LifestyleUnderwritingRequestDto requestDto) {

        log.info("Submitting lifestyle assessment for caseId: {}", caseId);

        UnderwritingCase underwritingCase = underwritingCaseRepository
                .findById(caseId)
                .orElseThrow(() -> {
                    log.warn("Case ID not found: {}", caseId);
                    return new CaseIdNotFound(
                            "Underwriting case not found with ID: " + caseId);
                });

        MedicalUnderwriting medicalUnderwriting = medicalUnderwritingRepository
                .findFirstByUnderwritingCase(underwritingCase)
                .orElseThrow(() -> new BusinessException(
                        "Submit medical assessment before lifestyle assessment"));

        medicalUnderwriting.setSmoking(requestDto.getSmoking());
        medicalUnderwriting.setAlcohol(requestDto.getAlcoholConsumption());
        medicalUnderwriting.setAdventureSports(requestDto.getAdventureSports());
        medicalUnderwriting.setFrequentInternationalTravel(
                requestDto.getFrequentInternationalTravel());
        medicalUnderwriting.setDangerousHobbies(requestDto.getDangerousHobbies());

        medicalUnderwriting = medicalUnderwritingRepository.save(medicalUnderwriting);

        LifestyleUnderwritingResponseDto responseDto =
                new LifestyleUnderwritingResponseDto();

        responseDto.setCaseId(underwritingCase.getUnderwritingId());
        responseDto.setMedicalId(medicalUnderwriting.getMedicalId());
        responseDto.setSmoking(medicalUnderwriting.getSmoking());
        responseDto.setAlcoholConsumption(medicalUnderwriting.getAlcohol());
        responseDto.setAdventureSports(medicalUnderwriting.getAdventureSports());
        responseDto.setFrequentInternationalTravel(
                medicalUnderwriting.getFrequentInternationalTravel());
        responseDto.setDangerousHobbies(medicalUnderwriting.getDangerousHobbies());

        log.info("Lifestyle assessment saved for caseId: {}", caseId);

        return new ApiResponse<>(
                true,
                "Lifestyle assessment submitted successfully",
                responseDto,
                LocalDateTime.now());
    }
    @Override
    public ApiResponse<FinancialUnderwritingResponseDto> getFinancialAssessment(Long caseId) {
        log.info("Fetching financial assessment for caseId: {}", caseId);
        // 2. Check whether case exists
        UnderwritingCase underwritingCase = underwritingCaseRepository
                .findById(caseId).orElseThrow(() ->
                        new CaseIdNotFound("Underwriting case not found with ID: " + caseId));

        // 3. Fetch financial assessment
        FinancialUnderwriting financial = financialUnderwritingRepository.findByUnderwritingCase_UnderwritingId(caseId)
                .orElseThrow(() -> new BusinessException(
                        "Financial assessment not found for caseId: " + caseId));
        // 4. Convert entity to response DTO
        FinancialUnderwritingResponseDto responseDto = modelMapper.map(financial, FinancialUnderwritingResponseDto.class);

        log.info("Financial assessment fetched successfully for caseId: {}", caseId);
        return new ApiResponse<>(true, "Financial assessment fetched successfully", responseDto, LocalDateTime.now()
        );
    }
    @Override
    @Transactional
    public ApiResponse<FinancialUnderwritingResponseDto> updateFinancialAssessment(Long caseId, FinancialUnderwritingRequestDto requestDto) {

        log.info("Updating financial assessment for caseId: {}", caseId);
        // 2. Check underwriting case exists
        UnderwritingCase underwritingCase =
                underwritingCaseRepository.findById(caseId).orElseThrow(() -> new CaseIdNotFound("Underwriting case not found with ID: " + caseId));
        // 3. Fetch existing financial assessment
        FinancialUnderwriting financial =
                financialUnderwritingRepository.findByUnderwritingCase_UnderwritingId(caseId).orElseThrow(() -> new BusinessException(
                                        "Financial assessment not found for caseId: " + caseId));
        // 4. Validate loan liability
        if (requestDto.getLoanLiability() != null
                && requestDto.getLoanLiability().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Loan liability cannot be negative");
        }
        // 5. Update only editable financial fields
        if (requestDto.getEmployerName() != null) {
            financial.setEmployerName(requestDto.getEmployerName());
        }
        if (requestDto.getExistingInsurance() != null) {
            financial.setExistingInsurance(requestDto.getExistingInsurance());
        }
        if (requestDto.getLoanLiability() != null) {
            financial.setLoanLiability(requestDto.getLoanLiability());
        }
        FinancialUnderwriting updatedFinancial = financialUnderwritingRepository.save(financial);
        FinancialUnderwritingResponseDto responseDto = modelMapper.map(updatedFinancial, FinancialUnderwritingResponseDto.class);
        log.info("Financial assessment updated successfully for caseId: {}", caseId);
        return new ApiResponse<>(true, "Financial assessment updated successfully", responseDto, LocalDateTime.now());
    }


    @Override
        public ApiResponse<RiskAssessmentResponseDto> calculateRisk(Long caseId) {

            UnderwritingCase underwritingCase = underwritingCaseRepository.findById(caseId)
                    .orElseThrow(() -> new CaseIdNotFound("Case not found"));

            MedicalUnderwriting medical = medicalUnderwritingRepository
                    .findByUnderwritingCase_UnderwritingId(caseId)
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Medical assessment not found"));

            FinancialUnderwriting financial = financialUnderwritingRepository
                    .findByUnderwritingCase_UnderwritingId(caseId)
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Financial assessment not found"));

            int ageScore = calculateAgeScore(medical.getAge());
            int medicalScore = calculateMedicalScore(medical);
            int occupationScore = calculateOccupationScore(financial.getOccupation());
            int lifestyleScore = calculateLifestyleScore(medical);
            int financialScore = calculateFinancialScore(financial);

            int totalScore = ageScore + medicalScore + occupationScore
                    + lifestyleScore + financialScore;

            RiskLevel riskCategory = totalScore <= 30 ? RiskLevel.LOW
                    : totalScore <= 60 ? RiskLevel.MEDIUM
                      : totalScore <= 80 ? RiskLevel.HIGH
                        : RiskLevel.VERY_HIGH;

            RiskAssessment assessment = new RiskAssessment();
            assessment.setUnderwritingCase(underwritingCase);
            assessment.setAgeScore(ageScore);
            assessment.setMedicalScore(medicalScore);
            assessment.setOccupationScore(occupationScore);
            assessment.setLifestyleScore(lifestyleScore);
            assessment.setFinancialScore(financialScore);
            assessment.setTotalScore(totalScore);
            assessment.setRiskCategory(riskCategory);

            assessment = riskAssessmentRepository.save(assessment);

            // LOW risk auto-approval
            if (riskCategory == RiskLevel.LOW) {

                UnderwritingDecision decision = new UnderwritingDecision();
                decision.setUnderwritingCase(underwritingCase);
                decision.setDecision(UnderwritingDecisionStatus.APPROVED);
                decision.setLoadingPercentage(BigDecimal.ZERO);
                decision.setDecisionDate(LocalDateTime.now());

                underwritingDecisionRepository.save(decision);

                underwritingCase.setStatus(UnderwritingStatus.APPROVED);

            } else {

                // MEDIUM, HIGH, VERY_HIGH: officer review karega
                underwritingCase.setStatus(UnderwritingStatus.PENDING);
            }

            underwritingCaseRepository.save(underwritingCase);

            RiskAssessmentResponseDto response =
                    modelMapper.map(assessment, RiskAssessmentResponseDto.class);
            response.setUnderwritingId(caseId);

            return new ApiResponse<>(true, "Risk calculated successfully",
                    response, LocalDateTime.now());
        }
    @Override
    public ApiResponse<UnderwritingDecisionResponseDto> calculatePremiumLoading(Long caseId) {

        UnderwritingCase underwritingCase = underwritingCaseRepository
                .findById(caseId)
                .orElseThrow(() -> new CaseIdNotFound("Case not found"));

        RiskAssessment risk = riskAssessmentRepository
                .findByUnderwritingCase_UnderwritingId(caseId)
                .orElseThrow(() -> new BusinessException("Risk assessment not found"));

        BigDecimal loadingPercentage;

        switch (risk.getRiskCategory()) {
            case LOW:
                loadingPercentage = BigDecimal.ZERO;
                break;
            case MEDIUM:
                loadingPercentage = new BigDecimal("10.00");
                break;
            case HIGH:
                loadingPercentage = new BigDecimal("25.00");
                break;
            case VERY_HIGH:
                loadingPercentage = new BigDecimal("50.00");
                break;
            default:
                throw new RuntimeException("Invalid risk category");
        }

        UnderwritingDecision decision = new UnderwritingDecision();
        decision.setUnderwritingCase(underwritingCase);
        decision.setLoadingPercentage(loadingPercentage);
        decision.setDecisionDate(LocalDateTime.now());

        UnderwritingDecision savedDecision =
                underwritingDecisionRepository.save(decision);

        UnderwritingDecisionResponseDto response =
                modelMapper.map(savedDecision, UnderwritingDecisionResponseDto.class);

        return new ApiResponse<>(
                true,
                "Premium loading calculated successfully",
                response,
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional
    public ApiResponse<UnderwritingDecisionResponseDto> approveCase(
            Long caseId, UnderwritingDecisionRequestDto requestDto) {

        log.info("Approving underwriting case: {}", caseId);

        // 1. Find case
        UnderwritingCase underwritingCase = underwritingCaseRepository
                .findById(caseId)
                .orElseThrow(() -> new CaseIdNotFound("Case not found"));

        // 2. Only pending cases can be approved
        if (underwritingCase.getStatus() != UnderwritingStatus.PENDING) {
            throw new RuntimeException("Only pending cases can be approved");
        }

        // 3. Validate officer ID
        if (requestDto == null || requestDto.getApprovedBy() == null) {
            throw new RuntimeException("Officer ID is required");
        }

        // 4. Find risk assessment
        RiskAssessment risk = riskAssessmentRepository
                .findByUnderwritingCase_UnderwritingId(caseId)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException("Risk assessment not found"));

        // 5. Calculate loading based on risk category
        BigDecimal loadingPercentage;

        switch (risk.getRiskCategory()) {
            case MEDIUM:
                loadingPercentage = new BigDecimal("10.00");
                break;

            case HIGH:
                loadingPercentage = new BigDecimal("25.00");
                break;

            case VERY_HIGH:
                loadingPercentage = new BigDecimal("50.00");
                break;

            case LOW:
                throw new BusinessException("Low-risk cases are auto-approved");

            default:
                throw new BusinessException("Invalid risk category");
        }

        // 6. Save decision
        UnderwritingDecision decision = new UnderwritingDecision();
        decision.setUnderwritingCase(underwritingCase);
        decision.setDecision(UnderwritingDecisionStatus.APPROVED_WITH_LOADING);
        decision.setLoadingPercentage(loadingPercentage);
        decision.setApprovedBy(requestDto.getApprovedBy());
        decision.setDecisionDate(LocalDateTime.now());

        underwritingDecisionRepository.save(decision);

        // 7. Update case status
        underwritingCase.setStatus(UnderwritingStatus.APPROVED);
        underwritingCaseRepository.save(underwritingCase);

        // 8. Prepare decision response
        UnderwritingDecisionResponseDto response =
                modelMapper.map(decision, UnderwritingDecisionResponseDto.class);

        log.info("Case {} approved by officer {} with {}% loading",
                caseId, requestDto.getApprovedBy(), loadingPercentage);

        return new ApiResponse<>(
                true,
                "Underwriting case approved successfully with "
                        + loadingPercentage + "% loading",
                response,
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional
    public ApiResponse<UnderwritingDecisionResponseDto> rejectCase(
            Long caseId, UnderwritingDecisionRequestDto requestDto) {

        log.info("Rejecting underwriting case: {}", caseId);

        // 1. Find case
        UnderwritingCase underwritingCase = underwritingCaseRepository.findById(caseId).orElseThrow(() -> new CaseIdNotFound("Case not found"));

        // 2. Only pending cases can be rejected
        if (underwritingCase.getStatus() != UnderwritingStatus.PENDING) {
            throw new BusinessException("Only pending cases can be rejected");
        }

        // 3. Validate request
        if (requestDto == null || requestDto.getApprovedBy() == null) {
            throw new BusinessException("Officer ID is required");
        }

        if (requestDto.getRejectionReason() == null || requestDto.getRejectionReason().isBlank()) {
            throw new BusinessException("Rejection reason is required");
        }

        // 4. Create rejection decision
        UnderwritingDecision decision = new UnderwritingDecision();
        decision.setUnderwritingCase(underwritingCase);
        decision.setDecision(UnderwritingDecisionStatus.REJECTED);
        decision.setApprovedBy(requestDto.getApprovedBy());
        decision.setRejectionReason(requestDto.getRejectionReason());
        decision.setDecisionDate(LocalDateTime.now());
        underwritingDecisionRepository.save(decision);

        // 5. Update case status
        underwritingCase.setStatus(UnderwritingStatus.REJECTED);
        underwritingCaseRepository.save(underwritingCase);

        // 6. Prepare response
        UnderwritingDecisionResponseDto response = modelMapper.map(decision, UnderwritingDecisionResponseDto.class);
        log.info("Case {} rejected by officer {}", caseId, requestDto.getApprovedBy());

        return new ApiResponse<>(true, "Underwriting case rejected successfully", response, LocalDateTime.now());
    }
    @Override
    public ApiResponse<UnderwritingDecisionResponseDto> getDecision(Long caseId) {
        log.info("Fetching underwriting decision for caseId: {}", caseId);
        // 2. Check whether underwriting case exists
        underwritingCaseRepository.findById(caseId).orElseThrow(() -> new CaseIdNotFound("Underwriting case not found with ID: " + caseId));
        // 3. Fetch decision for this case
        UnderwritingDecision decision = underwritingDecisionRepository.findByUnderwritingCase_UnderwritingId(caseId).orElseThrow(() ->
                                new BusinessException("Underwriting decision not found for caseId: " + caseId));
        // 4. Convert entity to response DTO
        UnderwritingDecisionResponseDto responseDto = modelMapper.map(decision, UnderwritingDecisionResponseDto.class);
        log.info("Underwriting decision fetched successfully for caseId: {}", caseId);
        // 5. Return response
        return new ApiResponse<>(true, "Underwriting decision fetched successfully", responseDto, LocalDateTime.now());
    }

    @Override
    public ApiResponse<List<AuditResponseDto>> getAuditHistory(long caseId) {
        log.info("Fetching audit history for case: {}", caseId);

        // 1. Check case exists
        underwritingCaseRepository.findById(caseId)
                .orElseThrow(() -> new CaseIdNotFound("Case not found"));

        // 2. Fetch audit logs
        List<UnderwritingAudit> auditLogs =
                underwritingAuditRepository.findByUnderwritingCase_UnderwritingIdOrderByActionDateDesc(caseId);

        // 3. Convert entity list to DTO list
        List<AuditResponseDto> response = auditLogs.stream().map(auditLog ->
                        modelMapper.map(auditLog, AuditResponseDto.class))
                .toList();

        return new ApiResponse<>(true, "Audit history fetched successfully", response, LocalDateTime.now());
    }



    private int calculateAgeScore(Integer age) {
        // Maximum score: 20
        if (age >= 60) return 20;
        if (age >= 45) return 15;
        if (age >= 30) return 10;
        return 5;
    }

    private int calculateMedicalScore(MedicalUnderwriting m) {
        int score = 0;
        if (Boolean.TRUE.equals(m.getDiabetes())) score += 10;
        if (Boolean.TRUE.equals(m.getHeartDisease())) score += 10;
        if (Boolean.TRUE.equals(m.getCancerHistory())) score += 10;
        return Math.min(score, 30);
    }

    private int calculateOccupationScore(String occupation) {
        if (occupation == null) return 10;

        switch (occupation.toLowerCase()) {
            case "software engineer":
            case "teacher": return 2;
            case "doctor": return 10;
            case "police officer":
            case "firefighter":
            case "pilot": return 15;
            case "miner": return 20;
            default: return 10;
        }
    }

    private int calculateLifestyleScore(MedicalUnderwriting m) {
        int score = 0;
        if (Boolean.TRUE.equals(m.getSmoking())) score += 8;
        if (Boolean.TRUE.equals(m.getAlcohol())) score += 4;
        if (Boolean.TRUE.equals(m.getAdventureSports())) score += 4;
        if (Boolean.TRUE.equals(m.getFrequentInternationalTravel())) score += 2;
        if (Boolean.TRUE.equals(m.getDangerousHobbies())) score += 2;
        return Math.min(score, 20);
    }
    private int calculateFinancialScore(FinancialUnderwriting f) {
        if (f.getAnnualIncome() == null ||
                f.getAnnualIncome().compareTo(BigDecimal.ZERO) <= 0) {
            return 10;
        }

        int score = 0;

        if (f.getLoanLiability() != null &&
                f.getLoanLiability().compareTo(f.getAnnualIncome()) > 0) {
            score += 5;
        }

        if (f.getExistingInsurance() != null &&
                f.getExistingInsurance().compareTo(BigDecimal.ZERO) > 0) {
            score += 2;
        }

        return Math.min(score, 10);
    }

    @Override
    public ApiResponse<List<UnderwritingResponseDto>> getPendingCases() {
        List<UnderwritingCase> cases = underwritingCaseRepository.findByStatus(UnderwritingStatus.PENDING);
        List<UnderwritingResponseDto> response = cases.stream()
                .map(c -> modelMapper.map(c, UnderwritingResponseDto.class)).toList();

        return new ApiResponse<>(true, "Pending cases fetched successfully", response, LocalDateTime.now()
        );
    }
}