package org.example.insurance_policy_system.serviceimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.enums.CategoryStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.insurance_policy_system.client.AuditClient;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.*;

import org.example.insurance_policy_system.repository.*;
import org.example.insurance_policy_system.service.ProductConfigurationService;
import org.example.insurance_policy_system.exception.BusinessException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductConfigurationServiceImpl implements ProductConfigurationService {
    private final PremiumFrequencyRepository frequencies;
    private final ProductCommissionRepository commissions;
    private final InsuranceProductRepository products;
    private final ModelMapper mapper;
    private final AuditClient auditClient;

    public PremiumFrequencyResponseDto createFrequency(PremiumFrequencyRequestDto r) {
        PremiumFrequencyMaster e = mapper.map(r, PremiumFrequencyMaster.class);
        e.setStatus(CategoryStatus.ACTIVE);
        e = frequencies.save(e);
        audit("PREMIUM_FREQUENCY", e.getFrequencyId(), "CREATE");
        return frequencyDto(e);
    }

    @Transactional(readOnly = true)
    public List<PremiumFrequencyResponseDto> getFrequencies() {
        return frequencies.findAll().stream().map(this::frequencyDto).toList();
    }

    public ProductCommissionResponseDto saveCommission(Long productId, ProductCommissionRequestDto r) {
        InsuranceProduct p = products.findById(productId).orElseThrow(() -> new BusinessException("Product not found: " + productId));
        ProductCommission e = commissions.findByProduct_ProductId(productId).orElseGet(ProductCommission::new);
        e.setProduct(p);
        e.setFirstYearCommission(r.getFirstYearCommission());
        e.setRenewalCommission(r.getRenewalCommission());
        e.setBranchCommission(r.getBranchCommission());
        e = commissions.save(e);
        audit("PRODUCT_COMMISSION", e.getCommissionId(), "SAVE");
        ProductCommissionResponseDto d = mapper.map(e, ProductCommissionResponseDto.class);
        d.setProductId(productId);
        return d;
    }

    @Transactional(readOnly = true)
    public ProductCommissionResponseDto getCommission(Long productId) {
        products.findById(productId).orElseThrow(() -> new BusinessException("Product not found: " + productId));
        ProductCommission e = commissions.findByProduct_ProductId(productId).orElseThrow(() -> new BusinessException("Commission configuration not found for productId: " + productId));
        ProductCommissionResponseDto d = mapper.map(e, ProductCommissionResponseDto.class);
        d.setProductId(productId);
        return d;
    }

    private PremiumFrequencyResponseDto frequencyDto(PremiumFrequencyMaster e) {
        PremiumFrequencyResponseDto d = mapper.map(e, PremiumFrequencyResponseDto.class);
        d.setStatus(e.getStatus().name());
        return d;
    }

    private void audit(String type, Long id, String action) {
        try {
            auditClient.writeAuditLog(new AuditLogRequestDto("PRODUCT", type, id, action, null, "Product configuration changed"));
        } catch (Exception ex) {
            log.warn("Audit service unavailable for {} id={}: {}", type, id, ex.getMessage());
        }
    }
}
