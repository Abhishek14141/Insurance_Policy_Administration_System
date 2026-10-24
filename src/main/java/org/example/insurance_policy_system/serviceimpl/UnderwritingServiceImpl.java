package org.example.insurance_policy_system.serviceimpl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.client.AuditClient;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.*;
import org.example.insurance_policy_system.entity.UnderwritingDecision;
import org.example.insurance_policy_system.enums.*;
import org.example.insurance_policy_system.exception.BusinessException;
import org.example.insurance_policy_system.exception.CaseIdNotFound;
import org.example.insurance_policy_system.repository.*;
import org.example.insurance_policy_system.service.UnderwritingService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnderwritingServiceImpl implements UnderwritingService {
    private final UnderwritingCaseRepository caseRepository;
    private final QuoteRepository quoteRepository;
    private final MedicalUnderwritingRepository medicalRepository;
    private final FinancialUnderwritingRepository financialRepository;
    private final RiskAssessmentRepository riskRepository;
    private final UnderwritingDecisionRepository decisionRepository;
    private final UnderwritingAuditRepository auditRepository;
    private final AuditClient auditClient;
    private final ModelMapper modelMapper;
    private final UnderwritingCaseRepository underwritingCaseRepository;
    private final RiskAssessmentRepository underwritingRiskRepository;
    private static final Path MEDICAL_REPORT_DIR =
            Paths.get("uploads", "medical-reports").toAbsolutePath().normalize();

    @Override
    @Transactional
    public ApiResponse<UnderwritingResponseDto> createCase(UnderwritingRequestDto request) {
        log.info("Creating underwriting case for quoteId={}", request.getQuoteId());
        Quote quote = quoteRepository.findById(request.getQuoteId()).orElseThrow(() -> new BusinessException("Quote not found: " + request.getQuoteId()));
        if (quote.getQuoteStatus() == QuoteStatus.REJECTED || quote.getQuoteStatus() == QuoteStatus.EXPIRED)
            throw new BusinessException("Rejected or expired quote cannot proceed to underwriting.");
        if (caseRepository.existsByQuoteId(request.getQuoteId()))
            throw new BusinessException("Underwriting case already exists for this quote.");
        UnderwritingCase entity = new UnderwritingCase();
        entity.setQuoteId(quote.getQuoteId());
        entity.setCustomerId(quote.getCustomerId());
        entity.setProductId(quote.getProductId());
        entity.setUnderwritingDate(LocalDate.now());
        entity.setStatus(UnderwritingStatus.PENDING);
        entity = caseRepository.save(entity);
        addAudit(entity, "CASE_CREATED", "Underwriting case created", null);
        return response("Underwriting case created successfully", modelMapper.map(entity, UnderwritingResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<UnderwritingResponseDto> getCase(Long id) {
        return response("Underwriting case fetched", modelMapper.map(findCase(id), UnderwritingResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<UnderwritingResponseDto>> getAllCases() {
        return response("Underwriting cases fetched", caseRepository.findAll().stream().map(c -> modelMapper.map(c, UnderwritingResponseDto.class)).collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<UnderwritingResponseDto>> getPendingCases() {
        return response("Pending cases fetched", caseRepository.findByStatus(UnderwritingStatus.PENDING).stream().map(c -> modelMapper.map(c, UnderwritingResponseDto.class)).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ApiResponse<UnderwritingResponseDto> updateCase(Long id, UnderwritingRequestDto request) {
        UnderwritingCase c = findCase(id);
        if (!c.getQuoteId().equals(request.getQuoteId())) {
            Quote q = quoteRepository.findById(request.getQuoteId()).orElseThrow(() -> new BusinessException("Quote not found."));
            if (caseRepository.existsByQuoteId(q.getQuoteId()))
                throw new BusinessException("Underwriting case already exists for this quote.");
            c.setQuoteId(q.getQuoteId());
            c.setCustomerId(q.getCustomerId());
            c.setProductId(q.getProductId());
        }
        c = caseRepository.save(c);
        addAudit(c, "CASE_UPDATED", "Underwriting case updated", null);
        return response("Underwriting case updated", modelMapper.map(c, UnderwritingResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<MedicalUnderwritingResponseDto> submitMedical(Long id, MedicalUnderwritingRequestDto request) {
        log.info("Submitting medical assessment caseId={}", id);
        UnderwritingCase c = findCase(id);
        MedicalUnderwriting m = medicalRepository.findByUnderwritingCase_UnderwritingId(id).orElseGet(MedicalUnderwriting::new);
        m.setUnderwritingCase(c);
        mapMedicalRequest(request, m);
        m.setBmi(calculateBmi(m.getHeight(), m.getWeight()));
        if (request.getMedicalReportPath() != null && !request.getMedicalReportPath().isBlank())
            m.setMedicalReportPath(request.getMedicalReportPath());
        m = medicalRepository.save(m);
        addAudit(c, "MEDICAL_SUBMITTED", "Medical assessment saved", null);
        return response("Medical assessment saved successfully", modelMapper.map(m, MedicalUnderwritingResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<MedicalUnderwritingResponseDto> getMedical(Long id) {
        findCase(id);
        MedicalUnderwriting m = medicalRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Medical assessment not found for caseId: " + id));
        return response("Medical assessment fetched", modelMapper.map(m, MedicalUnderwritingResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<MedicalUnderwritingResponseDto> updateMedical(Long id, MedicalUnderwritingRequestDto request) {
        UnderwritingCase c = findCase(id);
        MedicalUnderwriting m = medicalRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Medical assessment not found for caseId: " + id));
        String existingPath = m.getMedicalReportPath();
        mapMedicalRequest(request, m);
        m.setUnderwritingCase(c);
        m.setBmi(calculateBmi(m.getHeight(), m.getWeight()));
        if (request.getMedicalReportPath() == null || request.getMedicalReportPath().isBlank())
            m.setMedicalReportPath(existingPath);
        m = medicalRepository.save(m);
        addAudit(c, "MEDICAL_UPDATED", "Medical assessment updated", null);
        return response("Medical assessment updated", modelMapper.map(m, MedicalUnderwritingResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<MedicalUnderwritingResponseDto> uploadMedicalReport(Long id, MultipartFile file) {
        log.info("Uploading medical report caseId={}", id);
        UnderwritingCase c = findCase(id);
        if (file == null || file.isEmpty()) throw new BusinessException("Please select a medical report file.");
        try {
            Files.createDirectories(MEDICAL_REPORT_DIR);
            String original = file.getOriginalFilename() == null ? "medical-report" : Paths.get(file.getOriginalFilename()).getFileName().toString();
            String safeName = "case-" + id + "-" + System.currentTimeMillis() + "-" + original.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path destination = MEDICAL_REPORT_DIR.resolve(safeName).normalize();
            if (!destination.startsWith(MEDICAL_REPORT_DIR)) throw new BusinessException("Invalid report file path.");
            file.transferTo(destination);
            MedicalUnderwriting m = medicalRepository.findByUnderwritingCase_UnderwritingId(id).orElseGet(MedicalUnderwriting::new);
            m.setUnderwritingCase(c);
            m.setMedicalReportPath(destination.toString());
            m = medicalRepository.save(m);
            addAudit(c, "MEDICAL_REPORT_UPLOADED", "Medical report uploaded", null);
            return response("Medical report uploaded successfully", modelMapper.map(m, MedicalUnderwritingResponseDto.class));
        } catch (IOException ex) {
            log.error("Medical report upload failed for caseId={}", id, ex);
            throw new BusinessException("Unable to save medical report.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Resource viewMedicalReport(Long id) {
        log.info("Viewing medical report caseId={}", id);
        findCase(id);
        MedicalUnderwriting m = medicalRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Medical assessment not found."));
        if (m.getMedicalReportPath() == null || m.getMedicalReportPath().isBlank())
            throw new BusinessException("Medical report path is empty. Upload the report first.");
        Path path = Paths.get(m.getMedicalReportPath()).toAbsolutePath().normalize();
        if (!Files.exists(path) || !Files.isRegularFile(path))
            throw new BusinessException("Medical report file not found on server.");
        return new FileSystemResource(path);
    }

    @Override
    @Transactional
    public ApiResponse<FinancialUnderwritingResponseDto> submitFinancial(Long id, FinancialUnderwritingRequestDto request) {
        UnderwritingCase c = findCase(id);
        FinancialUnderwriting f = financialRepository.findByUnderwritingCase_UnderwritingId(id).orElseGet(FinancialUnderwriting::new);
        f.setUnderwritingCase(c);
        mapFinancialRequest(request, f);
        f = financialRepository.save(f);
        addAudit(c, "FINANCIAL_SUBMITTED", "Financial assessment saved", null);
        return response("Financial assessment saved", modelMapper.map(f, FinancialUnderwritingResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<FinancialUnderwritingResponseDto> getFinancial(Long id) {
        findCase(id);
        FinancialUnderwriting f = financialRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Financial assessment not found for caseId: " + id));
        return response("Financial assessment fetched", modelMapper.map(f, FinancialUnderwritingResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<FinancialUnderwritingResponseDto> updateFinancial(Long id, FinancialUnderwritingRequestDto request) {
        UnderwritingCase c = findCase(id);
        FinancialUnderwriting f = financialRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Financial assessment not found for caseId: " + id));
        String path = f.getFinancialDocumentPath();
        mapFinancialRequest(request, f);
        f.setUnderwritingCase(c);
        if (request.getFinancialDocumentPath() == null || request.getFinancialDocumentPath().isBlank())
            f.setFinancialDocumentPath(path);
        f = financialRepository.save(f);
        addAudit(c, "FINANCIAL_UPDATED", "Financial assessment updated", null);
        return response("Financial assessment updated", modelMapper.map(f, FinancialUnderwritingResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<LifestyleUnderwritingResponseDto> submitLifestyle(Long id, LifestyleUnderwritingRequestDto request) {
        UnderwritingCase c = findCase(id);
        MedicalUnderwriting m = medicalRepository.findByUnderwritingCase_UnderwritingId(id).orElseGet(MedicalUnderwriting::new);
        m.setUnderwritingCase(c);
        m.setSmoking(request.getSmoking());
        m.setAlcohol(request.getAlcoholConsumption());
        m.setAdventureSports(request.getAdventureSports());
        m.setFrequentInternationalTravel(request.getFrequentInternationalTravel());
        m.setDangerousHobbies(request.getDangerousHobbies());
        m.setLifestyleRemarks(request.getLifestyleRemarks());
        if (request.getLifestyleReportPath() != null && !request.getLifestyleReportPath().isBlank())
            m.setLifestyleReportPath(request.getLifestyleReportPath());
        m = medicalRepository.save(m);
        addAudit(c, "LIFESTYLE_SUBMITTED", "Lifestyle assessment saved", null);
        LifestyleUnderwritingResponseDto dto = new LifestyleUnderwritingResponseDto();
        dto.setCaseId(id);
        dto.setMedicalId(m.getMedicalId());
        dto.setSmoking(m.getSmoking());
        dto.setAlcoholConsumption(m.getAlcohol());
        dto.setAdventureSports(m.getAdventureSports());
        dto.setFrequentInternationalTravel(m.getFrequentInternationalTravel());
        dto.setDangerousHobbies(m.getDangerousHobbies());
        dto.setLifestyleRemarks(m.getLifestyleRemarks());
        dto.setLifestyleReportPath(m.getLifestyleReportPath());
        return response("Lifestyle assessment saved", dto);
    }

    @Override
    @Transactional
    public ApiResponse<RiskAssessmentResponseDto> calculateRisk(Long id) {
        log.info("Calculating underwriting risk factors caseId={}", id);
        UnderwritingCase c = findCase(id);
        MedicalUnderwriting m = medicalRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Submit medical assessment before calculating risk."));
        FinancialUnderwriting f = financialRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Submit financial assessment before calculating risk."));
        int age = scoreAge(m.getAge());
        int medical = 0;
        if (Boolean.TRUE.equals(m.getDiabetes())) medical += 15;
        if (Boolean.TRUE.equals(m.getHeartDisease())) medical += 25;
        if (Boolean.TRUE.equals(m.getCancerHistory())) medical += 30;
        if (m.getBmi() != null && (m.getBmi().compareTo(new BigDecimal("30")) >= 0 || m.getBmi().compareTo(new BigDecimal("18.5")) < 0))
            medical += 10;
        if (m.getBloodPressure() != null && !m.getBloodPressure().isBlank() && !m.getBloodPressure().trim().equalsIgnoreCase("normal") && !m.getBloodPressure().trim().equals("120/80"))
            medical += 5;
        // Empty medical report path is explicitly considered in the medical risk factor.
        if (m.getMedicalReportPath() == null || m.getMedicalReportPath().isBlank()) medical += 10;
        int occupation = scoreOccupation(f.getOccupation());
        int lifestyle = 0;
        if (Boolean.TRUE.equals(m.getSmoking())) lifestyle += 20;
        if (Boolean.TRUE.equals(m.getAlcohol())) lifestyle += 10;
        if (Boolean.TRUE.equals(m.getAdventureSports())) lifestyle += 10;
        if (Boolean.TRUE.equals(m.getFrequentInternationalTravel())) lifestyle += 5;
        if (Boolean.TRUE.equals(m.getDangerousHobbies())) lifestyle += 10;
        int financial = 0;
        if (f.getAnnualIncome() == null || f.getAnnualIncome().compareTo(new BigDecimal("300000")) < 0) financial += 20;
        if (f.getLoanLiability() != null && f.getAnnualIncome() != null && f.getLoanLiability().compareTo(f.getAnnualIncome()) > 0)
            financial += 15;
        if (f.getExistingInsurance() != null && f.getAnnualIncome() != null && f.getExistingInsurance().compareTo(f.getAnnualIncome().multiply(new BigDecimal("10"))) > 0)
            financial += 10;
        int total = Math.min(100, age + medical + occupation + lifestyle + financial);
        RiskLevel level = total <= 30 ? RiskLevel.LOW : total <= 60 ? RiskLevel.MEDIUM : total <= 80 ? RiskLevel.HIGH : RiskLevel.VERY_HIGH;
        RiskAssessment risk = riskRepository.findByUnderwritingCase_UnderwritingId(id).orElseGet(RiskAssessment::new);
        risk.setUnderwritingCase(c);
        risk.setAgeScore(age);
        risk.setMedicalScore(medical);
        risk.setOccupationScore(occupation);
        risk.setLifestyleScore(lifestyle);
        risk.setFinancialScore(financial);
        risk.setTotalScore(total);
        risk.setRiskCategory(level);
        risk = riskRepository.save(risk);
        if (level == RiskLevel.LOW) c.setStatus(UnderwritingStatus.APPROVED);
        else c.setStatus(UnderwritingStatus.PENDING);
        caseRepository.save(c);
        addAudit(c, "RISK_CALCULATED", "Risk score=" + total + ", category=" + level, null);
        return response("Risk calculated successfully", modelMapper.map(risk, RiskAssessmentResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<RiskAssessmentResponseDto> getRisk(Long id) {
        findCase(id);
        RiskAssessment risk = getRiskEntity(id);
        return response("Underwriting risk assessment fetched", modelMapper.map(risk, RiskAssessmentResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<UnderwritingDecisionResponseDto> approve(Long id, UnderwritingDecisionRequestDto request) {
        UnderwritingCase c = findCase(id);
        getRiskEntity(id);
        UnderwritingDecision d = decisionRepository.findByUnderwritingCase_UnderwritingId(id).orElseGet(UnderwritingDecision::new);
        d.setUnderwritingCase(c);
        d.setDecision(UnderwritingDecisionStatus.APPROVED);
        d.setApprovedBy(request.getApprovedBy());
        d.setDecisionDate(LocalDateTime.now());
        d = decisionRepository.save(d);
        c.setStatus(UnderwritingStatus.APPROVED);
        caseRepository.save(c);
        addAudit(c, "CASE_APPROVED", "Case approved", request.getApprovedBy());
        return response("Underwriting case approved", modelMapper.map(d, UnderwritingDecisionResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<UnderwritingDecisionResponseDto> reject(Long id, UnderwritingDecisionRequestDto request) {
        UnderwritingCase c = findCase(id);
        UnderwritingDecision d = decisionRepository.findByUnderwritingCase_UnderwritingId(id).orElseGet(UnderwritingDecision::new);
        d.setUnderwritingCase(c);
        d.setDecision(UnderwritingDecisionStatus.REJECTED);
        d.setApprovedBy(request.getApprovedBy());
        d.setRejectionReason(request.getRejectionReason());
        d.setDecisionDate(LocalDateTime.now());
        d = decisionRepository.save(d);
        c.setStatus(UnderwritingStatus.REJECTED);
        caseRepository.save(c);
        addAudit(c, "CASE_REJECTED", request.getRejectionReason(), request.getApprovedBy());
        return response("Underwriting case rejected", modelMapper.map(d, UnderwritingDecisionResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<UnderwritingDecisionResponseDto> applyLoading(Long id, BigDecimal loading, UnderwritingDecisionRequestDto request) {
        UnderwritingCase c = findCase(id);
        getRiskEntity(id);
        if (loading == null || loading.compareTo(BigDecimal.ZERO) < 0)
            throw new BusinessException("Loading percentage is required.");
        UnderwritingDecision d = decisionRepository.findByUnderwritingCase_UnderwritingId(id).orElseGet(UnderwritingDecision::new);
        d.setUnderwritingCase(c);
        d.setDecision(UnderwritingDecisionStatus.APPROVED_WITH_LOADING);
        d.setLoadingPercentage(loading);
        d.setApprovedBy(request.getApprovedBy());
        d.setDecisionDate(LocalDateTime.now());
        d = decisionRepository.save(d);
        c.setStatus(UnderwritingStatus.APPROVED);
        caseRepository.save(c);
        addAudit(c, "LOADING_APPLIED", "Loading=" + loading + "%", request.getApprovedBy());
        return response("Premium loading applied", modelMapper.map(d, UnderwritingDecisionResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<UnderwritingDecisionResponseDto> getDecision(Long id) {
        findCase(id);
        UnderwritingDecision d = decisionRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Underwriting decision not found."));
        return response("Underwriting decision fetched", modelMapper.map(d, UnderwritingDecisionResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<AuditResponseDto>> getAudit(Long id) {
        findCase(id);
        try {
            List<AuditResponseDto> external = auditClient.getUnderwritingAuditHistory(id);
            if (external != null && !external.isEmpty()) return response("Audit history fetched", external);
        } catch (FeignException ex) {
            log.warn("External audit service unavailable for caseId={}: {}", id, ex.getMessage());
        }
        return response("Audit history fetched", auditRepository.findByUnderwritingCase_UnderwritingIdOrderByActionDateDesc(id).stream().map(a -> {
            AuditResponseDto dto = new AuditResponseDto();
            dto.setAuditId(a.getAuditId());
            dto.setCaseId(id);
            dto.setAction(a.getAction());
            dto.setPerformedBy(a.getPerformedBy());
            dto.setRemarks(a.getRemarks());
            dto.setActionDate(a.getActionDate());
            return dto;
        }).collect(Collectors.toList()));
    }

    private UnderwritingCase findCase(Long id) {
        return caseRepository.findById(id).orElseThrow(() -> new CaseIdNotFound("Underwriting case not found: " + id));
    }

    private RiskAssessment getRiskEntity(Long id) {
        return riskRepository.findByUnderwritingCase_UnderwritingId(id).orElseThrow(() -> new BusinessException("Risk factors have not been calculated. Calculate risk first."));
    }

    private void mapMedicalRequest(MedicalUnderwritingRequestDto r, MedicalUnderwriting m) {
        modelMapper.map(r, m);
    }

    private void mapFinancialRequest(FinancialUnderwritingRequestDto r, FinancialUnderwriting f) {
        modelMapper.map(r, f);
    }

    private BigDecimal calculateBmi(BigDecimal heightCm, BigDecimal weightKg) {
        if (heightCm == null || weightKg == null || heightCm.compareTo(BigDecimal.ZERO) <= 0) return null;
        BigDecimal metres = heightCm.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        return weightKg.divide(metres.multiply(metres), 2, RoundingMode.HALF_UP);
    }

    private int scoreAge(Integer age) {
        if (age == null) return 10;
        if (age < 25) return 5;
        if (age <= 45) return 10;
        if (age <= 60) return 20;
        return 30;
    }

    private int scoreOccupation(String occupation) {
        if (occupation == null || occupation.isBlank()) return 10;
        String value = occupation.toLowerCase();
        if (value.contains("office") || value.contains("teacher") || value.contains("engineer") || value.contains("account"))
            return 5;
        if (value.contains("driver") || value.contains("factory") || value.contains("construction") || value.contains("police"))
            return 20;
        return 10;
    }

    private void addAudit(UnderwritingCase c, String action, String remarks, Long performedBy) {
        UnderwritingAudit audit = new UnderwritingAudit();
        audit.setUnderwritingCase(c);
        audit.setAction(action);
        audit.setRemarks(remarks);
        audit.setPerformedBy(performedBy);
        audit.setActionDate(LocalDateTime.now());
        auditRepository.save(audit);
        try {
            auditClient.writeAuditLog(new AuditLogRequestDto("UNDERWRITING", "UNDERWRITING_CASE", c.getUnderwritingId(), action, performedBy, remarks));
        } catch (FeignException ex) {
            log.warn("External audit service call failed for caseId={}: {}", c.getUnderwritingId(), ex.getMessage());
        }
    }

    private <T> ApiResponse<T> response(String message, T data) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).timestamp(LocalDateTime.now()).build();
    }

    @Override
    public PolicyUnderwritingDetailsDto getPolicyDetailsByQuoteId(Long quoteId) {

        log.info("Fetching underwriting details for quoteId: {}", quoteId);

        UnderwritingCase underwritingCase =
                underwritingCaseRepository.findByQuoteId(quoteId)
                        .orElseThrow(() -> new BusinessException(
                                        "Underwriting case not found for quoteId: " + quoteId));

        Long caseId = underwritingCase.getUnderwritingId();
        log.info("Underwriting case found. CaseId: {}", caseId);
        RiskAssessment risk = underwritingRiskRepository.findByUnderwritingCase_UnderwritingId(caseId)
                .orElse(null);

        log.info("Risk assessment fetched: {}", risk != null);

        UnderwritingDecision decision = decisionRepository
                .findByUnderwritingCase_UnderwritingId(caseId)
                .orElse(null);

        log.info("Underwriting decision fetched: {}", decision != null);
        PolicyUnderwritingDetailsDto dto = new PolicyUnderwritingDetailsDto();
        dto.setUnderwritingId(caseId);
        dto.setQuoteId(underwritingCase.getQuoteId());
        dto.setCustomerId(underwritingCase.getCustomerId());
        dto.setProductId(underwritingCase.getProductId());

        dto.setUnderwritingStatus(underwritingCase.getStatus() != null ? underwritingCase.getStatus().name() : null);

        if (risk != null) {
            dto.setTotalScore(risk.getTotalScore());
            dto.setRiskCategory(risk.getRiskCategory() != null ? risk.getRiskCategory().name() : null);
        }

        if (decision != null) {
            dto.setDecision(decision.getDecision() != null
                    ? decision.getDecision().name() : null);
            dto.setLoadingPercentage(decision.getLoadingPercentage());
            dto.setApprovedBy(decision.getApprovedBy());
            dto.setDecisionDate(decision.getDecisionDate());
        }

        log.info("Policy underwriting details fetched successfully for quoteId: {}", quoteId);

        return dto;
    }
}