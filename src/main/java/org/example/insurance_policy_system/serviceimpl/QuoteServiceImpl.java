package org.example.insurance_policy_system.serviceimpl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.client.AuditClient;
import org.example.insurance_policy_system.client.KycClient;
import org.example.insurance_policy_system.client.CustomerClient;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.*;
import org.example.insurance_policy_system.enums.*;
import org.example.insurance_policy_system.exception.BusinessException;
import org.example.insurance_policy_system.repository.*;
import org.example.insurance_policy_system.service.QuoteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;
    private final InsuranceProductRepository productRepository;
    private final PremiumFrequencyRepository frequencyRepository;
    private final PremiumCalculationRepository premiumRepository;
    private final QuoteRiskAssessmentRepository quoteRiskRepository;
    private final QuoteHistoryRepository historyRepository;
    private final QuoteRiderRepository quoteRiderRepository;
    private final RiderRepository riderRepository;
    private final DiscountMasterRepository discountRepository;
    private final UnderwritingCaseRepository underwritingCaseRepository;
    private final RiskAssessmentRepository underwritingRiskRepository;
    private final KycClient kycClient;
    private final CustomerClient customerClient;
    private final AuditClient auditClient;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ApiResponse<QuoteResponseDto> createQuote(QuoteRequestDto request) {
        log.info("Creating quote for customerId={}, productId={}", request.getCustomerId(), request.getProductId());
        KycResponse kyc = kycClient.getKycByCustomerId(request.getCustomerId());
        String kycStatus = kyc == null ? null : kyc.getStatus();
        if (kycStatus == null || !(kycStatus.equalsIgnoreCase("VERIFIED") || kycStatus.equalsIgnoreCase("APPROVED"))) {
            throw new BusinessException("Customer KYC is not approved. Quote cannot be created.");
        }
        CustomerResponse customer = customerClient.getById(request.getCustomerId());
        InsuranceProduct product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException("Insurance product not found: " + request.getProductId()));
        if (product.getStatus() != ProductStatus.ACTIVE)
            throw new BusinessException("Only active products can be quoted.");
        if (product.getMinSumInsured() != null && request.getSumInsured().compareTo(product.getMinSumInsured()) < 0)
            throw new BusinessException("Sum insured is below product minimum.");
        if (product.getMaxSumInsured() != null && request.getSumInsured().compareTo(product.getMaxSumInsured()) > 0)
            throw new BusinessException("Sum insured exceeds product maximum.");
        if (product.getMinTermYears() != null && request.getPolicyTerm() < product.getMinTermYears())
            throw new BusinessException("Policy term is below product minimum.");
        if (product.getMaxTermYears() != null && request.getPolicyTerm() > product.getMaxTermYears())
            throw new BusinessException("Policy term exceeds product maximum.");
        frequencyRepository.findById(request.getPremiumFrequencyId())
                .orElseThrow(() -> new BusinessException("Premium frequency not found."));
        if (customer != null && customer.getAge() != null) {
            if (product.getMinimumAge() != null && customer.getAge() < product.getMinimumAge())
                throw new BusinessException("Customer age is below product eligibility.");
            if (product.getMaximumAge() != null && customer.getAge() > product.getMaximumAge())
                throw new BusinessException("Customer age exceeds product eligibility.");
        }

        Quote quote = modelMapper.map(request, Quote.class);
        quote.setQuoteNumber(generateQuoteNumber());
        quote.setQuoteDate(LocalDate.now());
        quote.setExpiryDate(LocalDate.now().plusDays(30));
        quote.setQuoteStatus(QuoteStatus.PENDING);
        quote = quoteRepository.save(quote);
        saveHistory(quote, "CREATED", "Quote created", null);
        writeAudit("CREATE", quote.getQuoteId(), "Quote created for customer " + quote.getCustomerId());
        log.info("Quote created successfully quoteId={}", quote.getQuoteId());
        return response("Quote created successfully", modelMapper.map(quote, QuoteResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<QuoteResponseDto> getQuote(Long quoteId) {
        log.info("Getting quote quoteId={}", quoteId);
        return response("Quote fetched successfully",
                modelMapper.map(findQuote(quoteId), QuoteResponseDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<QuoteResponseDto>> getAllQuotes() {
        log.info("Getting all quotes");
        return response("Quotes fetched successfully", quoteRepository.findAll().stream()
                .map(q -> modelMapper.map(q, QuoteResponseDto.class))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ApiResponse<QuoteResponseDto> updateQuote(Long quoteId, QuoteRequestDto request) {
        log.info("Updating quote quoteId={}", quoteId);
        Quote quote = findQuote(quoteId);
        if (quote.getQuoteStatus() == QuoteStatus.REJECTED || quote.getQuoteStatus() == QuoteStatus.EXPIRED)
            throw new BusinessException("Rejected or expired quote cannot be updated.");
        InsuranceProduct product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException("Insurance product not found."));
        if (product.getStatus() != ProductStatus.ACTIVE)
            throw new BusinessException("Only active products can be quoted.");
        modelMapper.map(request, quote);
        quote.setExpiryDate(LocalDate.now().plusDays(30));
        quote = quoteRepository.save(quote);
        saveHistory(quote, "UPDATED", "Quote updated", null);
        writeAudit("UPDATE", quoteId, "Quote details updated");
        return response("Quote updated successfully", modelMapper.map(quote, QuoteResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<QuoteResponseDto> approveQuote(Long quoteId) {
        log.info("Approving quoteId={}", quoteId);
        Quote quote = findQuote(quoteId);
        if (quote.getQuoteStatus() == QuoteStatus.REJECTED || quote.getQuoteStatus() == QuoteStatus.EXPIRED)
            throw new BusinessException("Rejected or expired quote cannot be approved.");
        quote.setQuoteStatus(QuoteStatus.APPROVED);
        quote = quoteRepository.save(quote);
        saveHistory(quote, "APPROVED", "Quote approved", null);
        writeAudit("APPROVED", quoteId, "Quote approved");
        return response("Quote approved successfully", modelMapper.map(quote, QuoteResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<QuoteResponseDto> rejectQuote(Long quoteId, String remarks) {
        log.info("Rejecting quoteId={}", quoteId);
        Quote quote = findQuote(quoteId);
        if (quote.getQuoteStatus() == QuoteStatus.EXPIRED)
            throw new BusinessException("Expired quote cannot be rejected.");
        quote.setQuoteStatus(QuoteStatus.REJECTED);
        quote = quoteRepository.save(quote);
        saveHistory(quote, "REJECTED", remarks == null || remarks.isBlank() ? "Quote rejected" : remarks, null);
        writeAudit("REJECTED", quoteId, remarks == null || remarks.isBlank() ? "Quote rejected" : remarks);
        return response("Quote rejected successfully", modelMapper.map(quote, QuoteResponseDto.class));
    }

    @Override
    @Transactional
    public ApiResponse<PremiumCalculationResponseDto> calculatePremium(PremiumCalculationRequestDto request) {
        log.info("Calculating premium for quoteId={}", request.getQuoteId());
        Quote quote = findQuote(request.getQuoteId());
        if (quote.getExpiryDate() != null && quote.getExpiryDate().isBefore(LocalDate.now())) {
            quote.setQuoteStatus(QuoteStatus.EXPIRED);
            quoteRepository.save(quote);
            saveHistory(quote, "EXPIRED", "Quote expired", null);
            throw new BusinessException("Quote has expired.");
        }
        InsuranceProduct product = productRepository.findById(quote.getProductId())
                .orElseThrow(() -> new BusinessException("Insurance product not found."));
        BigDecimal rate = product.getProductRate() == null ? new BigDecimal("2.500") : product.getProductRate();
        BigDecimal base = quote.getSumInsured().multiply(rate).divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP);

        BigDecimal riderTotal = BigDecimal.ZERO;
        quoteRiderRepository.deleteByQuote_QuoteId(quote.getQuoteId());
        if (request.getRiderIds() != null) {
            for (Long riderId : request.getRiderIds()) {
                Rider rider = riderRepository.findById(riderId).orElseThrow(() -> new BusinessException("Rider not found: " + riderId));
                if (!rider.getProduct().getProductId().equals(product.getProductId()) || rider.getStatus() != RiderStatus.ACTIVE)
                    throw new BusinessException("Selected rider is not active for this product.");
                BigDecimal riderPremium = rider.getRiderPremium() == null ? BigDecimal.ZERO : rider.getRiderPremium();
                riderTotal = riderTotal.add(riderPremium);
                QuoteRider selected = new QuoteRider();
                selected.setQuote(quote);
                selected.setRider(rider);
                selected.setRiderPremium(riderPremium);
                quoteRiderRepository.save(selected);
            }
        }
        BigDecimal riskLoading = BigDecimal.ZERO;
        QuoteRiskAssessment savedQuoteRisk = quoteRiskRepository.findFirstByQuote_QuoteIdOrderByRiskIdDesc(quote.getQuoteId()).orElse(null);
        if (savedQuoteRisk != null && savedQuoteRisk.getRiskCategory() != QuoteRiskCategory.LOW)
            riskLoading = base.multiply(new BigDecimal("0.10"));
        BigDecimal discount = BigDecimal.ZERO;
        if (request.getDiscountId() != null) {
            DiscountMaster master = discountRepository.findById(request.getDiscountId()).orElseThrow(() -> new BusinessException("Discount not found."));
            LocalDate today = LocalDate.now();
            if (master.getStatus() != DiscountStatus.ACTIVE || (master.getValidFrom() != null && today.isBefore(master.getValidFrom())) || (master.getValidTo() != null && today.isAfter(master.getValidTo())))
                throw new BusinessException("Discount is not active for today.");
            discount = base.multiply(master.getDiscountPercentage()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        BigDecimal beforeGst = base.add(riskLoading).add(riderTotal).subtract(discount);
        if (beforeGst.compareTo(BigDecimal.ZERO) < 0) beforeGst = BigDecimal.ZERO;
        BigDecimal gst = beforeGst.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        PremiumCalculation calculation = new PremiumCalculation();
        calculation.setQuote(quote);
        calculation.setBasePremium(base);
        calculation.setRiskLoading(riskLoading);
        calculation.setRiderPremium(riderTotal);
        calculation.setDiscountAmount(discount);
        calculation.setGstAmount(gst);
        calculation.setFinalPremium(beforeGst.add(gst));
        calculation.setCalculatedAt(LocalDateTime.now());
        calculation = premiumRepository.save(calculation);
        writeAudit("PREMIUM_CALCULATED", quote.getQuoteId(), "Premium calculated");
        return response("Premium calculated successfully", modelMapper.map(calculation, PremiumCalculationResponseDto.class));
    }

    @Transactional(readOnly = true)
    public PremiumCalculationResponseDto getPremium(Long quoteId) {
        findQuote(quoteId);
        PremiumCalculation c = premiumRepository.findFirstByQuote_QuoteIdOrderByCalculatedAtDesc(quoteId).orElseThrow(() -> new BusinessException("Premium calculation not found for quoteId: " + quoteId));
        PremiumCalculationResponseDto d = modelMapper.map(c, PremiumCalculationResponseDto.class);
        d.setQuoteId(quoteId);
        return d;
    }
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<QuoteHistoryResponseDto>> getHistory(Long quoteId) {
        findQuote(quoteId);
        return response("Quote history fetched successfully", historyRepository.findByQuote_QuoteIdOrderByActionDateDesc(quoteId)
                .stream().map(h -> modelMapper.map(h, QuoteHistoryResponseDto.class)).collect(Collectors.toList()));
    }

    private Quote findQuote(Long id) {
        return quoteRepository.findById(id).orElseThrow(() -> new BusinessException("Quote not found: " + id));
    }

    private String generateQuoteNumber() {
        return "QT-" + LocalDate.now().toString().replace("-", "") + "-" + (quoteRepository.count() + 1);
    }

    private void saveHistory(Quote quote, String action, String remarks, Long performedBy) {
        QuoteHistory h = new QuoteHistory();
        h.setQuote(quote);
        h.setAction(action);
        h.setRemarks(remarks);
        h.setPerformedBy(performedBy);
        h.setActionDate(LocalDateTime.now());
        historyRepository.save(h);
    }

    private void writeAudit(String action, Long id, String remarks) {
        try {
            auditClient.writeAuditLog(new AuditLogRequestDto("QUOTE", "QUOTE", id, action, null, remarks));
        } catch (FeignException ex) {
            log.warn("Audit service call failed for quoteId={}: {}", id, ex.getMessage());
        }
    }

    private QuoteRiskCategory mapCategory(RiskLevel level) {
        if (level == null) throw new BusinessException("Underwriting risk category is empty.");
        return switch (level) {
            case LOW -> QuoteRiskCategory.LOW;
            case MEDIUM -> QuoteRiskCategory.MEDIUM;
            case HIGH, VERY_HIGH -> QuoteRiskCategory.HIGH;
        };
    }

    private <T> ApiResponse<T> response(String message, T data) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).timestamp(LocalDateTime.now()).build();
    }
}
