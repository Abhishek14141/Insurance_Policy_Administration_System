package org.example.insurance_policy_system.configuration;

import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

        mapper.typeMap(Coverage.class, CoverageResponseDto.class)
                .addMappings(m -> m.map(src -> src.getProduct().getProductId(), CoverageResponseDto::setProductId));
        mapper.typeMap(Rider.class, RiderResponseDto.class)
                .addMappings(m -> m.map(src -> src.getProduct().getProductId(), RiderResponseDto::setProductId));
        mapper.typeMap(Benefit.class, BenefitResponseDto.class)
                .addMappings(m -> m.map(src -> src.getProduct().getProductId(), BenefitResponseDto::setProductId));
        mapper.typeMap(PremiumCalculation.class, PremiumCalculationResponseDto.class)
                .addMappings(m -> m.map(src -> src.getQuote().getQuoteId(), PremiumCalculationResponseDto::setQuoteId));
        mapper.typeMap(QuoteRiskAssessment.class, QuoteRiskAssessmentResponseDto.class)
                .addMappings(m -> m.map(src -> src.getQuote().getQuoteId(), QuoteRiskAssessmentResponseDto::setQuoteId));
        mapper.typeMap(QuoteRider.class, QuoteRiderResponseDto.class).addMappings(m -> {
            m.map(src -> src.getQuote().getQuoteId(), QuoteRiderResponseDto::setQuoteId);
            m.map(src -> src.getRider().getRiderId(), QuoteRiderResponseDto::setRiderId);
        });
        mapper.typeMap(QuoteHistory.class, QuoteHistoryResponseDto.class)
                .addMappings(m -> m.map(src -> src.getQuote().getQuoteId(), QuoteHistoryResponseDto::setQuoteId));
        mapper.typeMap(ProductCommission.class, ProductCommissionResponseDto.class)
                .addMappings(m -> m.map(src -> src.getProduct().getProductId(), ProductCommissionResponseDto::setProductId));
        mapper.typeMap(FinancialUnderwriting.class, FinancialUnderwritingResponseDto.class)
                .addMappings(m -> m.map(src -> src.getUnderwritingCase().getUnderwritingId(), FinancialUnderwritingResponseDto::setUnderwritingId));
        mapper.typeMap(MedicalUnderwriting.class, MedicalUnderwritingResponseDto.class)
                .addMappings(m -> m.map(src -> src.getUnderwritingCase().getUnderwritingId(), MedicalUnderwritingResponseDto::setUnderwritingId));
        mapper.typeMap(RiskAssessment.class, RiskAssessmentResponseDto.class)
                .addMappings(m -> m.map(src -> src.getUnderwritingCase().getUnderwritingId(), RiskAssessmentResponseDto::setUnderwritingId));
        mapper.typeMap(UnderwritingDecision.class, UnderwritingDecisionResponseDto.class)
                .addMappings(m -> m.map(src -> src.getUnderwritingCase().getUnderwritingId(), UnderwritingDecisionResponseDto::setUnderwritingId));
        mapper.typeMap(UnderwritingAudit.class, AuditResponseDto.class)
                .addMappings(m -> m.map(src -> src.getUnderwritingCase().getUnderwritingId(), AuditResponseDto::setCaseId));
        mapper.typeMap(ProductRequestDto.class, InsuranceProduct.class)
                .addMappings(m -> m.skip(InsuranceProduct::setCategory));
        mapper.typeMap(MedicalUnderwritingRequestDto.class, MedicalUnderwriting.class)
                .addMappings(m -> m.skip(MedicalUnderwriting::setMedicalReportPath));
        return mapper;
    }
}
