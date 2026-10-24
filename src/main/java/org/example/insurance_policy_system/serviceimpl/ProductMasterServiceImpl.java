package org.example.insurance_policy_system.serviceimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.insurance_policy_system.client.AuditClient;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.*;
import org.example.insurance_policy_system.repository.*;
import org.example.insurance_policy_system.service.ProductMasterService;
import org.example.insurance_policy_system.exception.BusinessException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductMasterServiceImpl implements ProductMasterService {
    private final CoverageRepository coverages;
    private final RiderRepository riders;
    private final BenefitRepository benefits;
    private final InsuranceProductRepository products;
    private final ModelMapper mapper;
    private final AuditClient auditClient;

    private InsuranceProduct product(Long id) {
        return products.findById(id).orElseThrow(() -> new BusinessException("Product not found: " + id));
    }

    private void audit(String action, String type, Long id) {
        log.info("Audit action={} entityType={} entityId={}", action, type, id);
        try {
            auditClient.writeAuditLog(new AuditLogRequestDto("PRODUCT", type, id, action, null, "Product master operation"));
        } catch (Exception ex) {
            log.warn("Audit service unavailable for {} id={}: {}", type, id, ex.getMessage());
        }
    }

    public CoverageResponseDto addCoverage(Long pid, CoverageRequestDto d) {
        InsuranceProduct p = product(pid);
        if (d.getMinimumCoverage().compareTo(d.getMaximumCoverage()) > 0)
            throw new BusinessException("Minimum coverage cannot exceed maximum coverage");
        Coverage e = mapper.map(d, Coverage.class);
        e.setProduct(p);
        e = coverages.save(e);
        audit("CREATE", "COVERAGE", e.getCoverageId());
        return coverageDto(e);
    }

    @Transactional(readOnly = true)
    public List<CoverageResponseDto> getCoverages(Long pid) {
        product(pid);
        return coverages.findByProduct_ProductId(pid).stream().map(this::coverageDto).toList();
    }

    public CoverageResponseDto updateCoverage(Long id, CoverageRequestDto d) {
        Coverage e = coverages.findById(id).orElseThrow(() -> new BusinessException("Coverage not found: " + id));
        if (d.getMinimumCoverage().compareTo(d.getMaximumCoverage()) > 0)
            throw new BusinessException("Minimum coverage cannot exceed maximum coverage");
        mapper.map(d, e);
        e = coverages.save(e);
        audit("UPDATE", "COVERAGE", id);
        return coverageDto(e);
    }

    public void deleteCoverage(Long id) {
        Coverage e = coverages.findById(id).orElseThrow(() -> new BusinessException("Coverage not found: " + id));
        coverages.delete(e);
        audit("DELETE", "COVERAGE", id);
    }

    private CoverageResponseDto coverageDto(Coverage e) {
        CoverageResponseDto d = mapper.map(e, CoverageResponseDto.class);
        d.setProductId(e.getProduct().getProductId());
        return d;
    }

    public RiderResponseDto addRider(Long pid, RiderRequestDto d) {
        Rider e = mapper.map(d, Rider.class);
        e.setProduct(product(pid));
        e = riders.save(e);
        audit("CREATE", "RIDER", e.getRiderId());
        return riderDto(e);
    }

    @Transactional(readOnly = true)
    public List<RiderResponseDto> getRiders(Long pid) {
        product(pid);
        return riders.findByProduct_ProductId(pid).stream().map(this::riderDto).toList();
    }

    public RiderResponseDto updateRider(Long id, RiderRequestDto d) {
        Rider e = riders.findById(id).orElseThrow(() -> new BusinessException("Rider not found: " + id));
        mapper.map(d, e);
        e = riders.save(e);
        audit("UPDATE", "RIDER", id);
        return riderDto(e);
    }

    public void deleteRider(Long id) {
        Rider e = riders.findById(id).orElseThrow(() -> new BusinessException("Rider not found: " + id));
        riders.delete(e);
        audit("DELETE", "RIDER", id);
    }

    private RiderResponseDto riderDto(Rider e) {
        RiderResponseDto d = mapper.map(e, RiderResponseDto.class);
        d.setProductId(e.getProduct().getProductId());
        d.setStatus(e.getStatus().name());
        return d;
    }

    public BenefitResponseDto addBenefit(Long pid, BenefitRequestDto d) {
        Benefit e = mapper.map(d, Benefit.class);
        e.setProduct(product(pid));
        e = benefits.save(e);
        audit("CREATE", "BENEFIT", e.getBenefitId());
        return benefitDto(e);
    }

    @Transactional(readOnly = true)
    public List<BenefitResponseDto> getBenefits(Long pid) {
        product(pid);
        return benefits.findByProduct_ProductId(pid).stream().map(this::benefitDto).toList();
    }

    public BenefitResponseDto updateBenefit(Long id, BenefitRequestDto d) {
        Benefit e = benefits.findById(id).orElseThrow(() -> new BusinessException("Benefit not found: " + id));
        mapper.map(d, e);
        e = benefits.save(e);
        audit("UPDATE", "BENEFIT", id);
        return benefitDto(e);
    }

    public void deleteBenefit(Long id) {
        Benefit e = benefits.findById(id).orElseThrow(() -> new BusinessException("Benefit not found: " + id));
        benefits.delete(e);
        audit("DELETE", "BENEFIT", id);
    }

    private BenefitResponseDto benefitDto(Benefit e) {
        BenefitResponseDto d = mapper.map(e, BenefitResponseDto.class);
        d.setProductId(e.getProduct().getProductId());
        d.setStatus(e.getStatus().name());
        return d;
    }
}
