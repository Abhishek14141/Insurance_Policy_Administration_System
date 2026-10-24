package org.example.insurance_policy_system.serviceimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.insurance_policy_system.client.AuditClient;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.*;
import org.example.insurance_policy_system.enums.ProductStatus;
import org.example.insurance_policy_system.repository.*;
import org.example.insurance_policy_system.service.ProductService;
import org.example.insurance_policy_system.exception.BusinessException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {
    private final InsuranceProductRepository products;
    private final ProductCategoryRepository categories;
    private final ModelMapper mapper;
    private final AuditClient auditClient;

    public ProductResponseDto create(ProductRequestDto r) {
        log.info("Creating product code={}", r.getProductCode());
        if (products.existsByProductCode(r.getProductCode()))
            throw new BusinessException("Product code already exists");
        validate(r);
        ProductCategory c = categories.findById(r.getCategoryId()).orElseThrow(() -> new BusinessException("Product category not found: " + r.getCategoryId()));
        InsuranceProduct e = mapper.map(r, InsuranceProduct.class);
        e.setCategory(c);
        e.setStatus(ProductStatus.INACTIVE);
        if (e.getProductRate() == null) e.setProductRate(new java.math.BigDecimal("2.500"));
        e = products.save(e);
        audit("CREATE", "PRODUCT", e.getProductId(), "Product created");
        return dto(e);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto get(Long id) {
        return dto(find(id));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAll() {
        return products.findAll().stream().map(this::dto).toList();
    }

    public ProductResponseDto update(Long id, ProductRequestDto r) {
        InsuranceProduct e = find(id);
        validate(r);
        if (!e.getProductCode().equals(r.getProductCode()) && products.existsByProductCode(r.getProductCode()))
            throw new BusinessException("Product code already exists");
        ProductCategory c = categories.findById(r.getCategoryId()).orElseThrow(() -> new BusinessException("Product category not found: " + r.getCategoryId()));
        mapper.map(r, e);
        e.setCategory(c);
        e = products.save(e);
        audit("UPDATE", "PRODUCT", id, "Product details updated");
        return dto(e);
    }

    public ProductResponseDto updateStatus(Long id, String status) {
        ProductStatus value;
        try {
            value = ProductStatus.valueOf(status.trim().toUpperCase());
        } catch (Exception ex) {
            throw new BusinessException("Status must be ACTIVE or INACTIVE");
        }
        InsuranceProduct e = find(id);
        e.setStatus(value);
        e = products.save(e);
        audit("STATUS_UPDATE", "PRODUCT", id, "Status changed to " + value);
        return dto(e);
    }

    public void deactivate(Long id) {
        InsuranceProduct e = find(id);
        e.setStatus(ProductStatus.INACTIVE);
        products.save(e);
        audit("DEACTIVATE", "PRODUCT", id, "Product deactivated");
    }

    private InsuranceProduct find(Long id) {
        return products.findById(id).orElseThrow(() -> new BusinessException("Product not found: " + id));
    }

    private void validate(ProductRequestDto r) {
        if (r.getMinSumInsured().compareTo(r.getMaxSumInsured()) > 0)
            throw new BusinessException("Minimum sum insured cannot exceed maximum");
        if (r.getMinTermYears() != null && r.getMaxTermYears() != null && r.getMinTermYears() > r.getMaxTermYears())
            throw new BusinessException("Minimum term cannot exceed maximum term");
        if (r.getMinimumAge() != null && r.getMaximumAge() != null && r.getMinimumAge() > r.getMaximumAge())
            throw new BusinessException("Minimum age cannot exceed maximum age");
    }

    private ProductResponseDto dto(InsuranceProduct e) {
        ProductResponseDto d = mapper.map(e, ProductResponseDto.class);
        d.setCategoryId(e.getCategory().getCategoryId());
        d.setCategoryName(e.getCategory().getCategoryName());
        d.setStatus(e.getStatus().name());
        return d;
    }

    private void audit(String action, String type, Long id, String remarks) {
        try {
            auditClient.writeAuditLog(new AuditLogRequestDto("PRODUCT", type, id, action, null, remarks));
        } catch (Exception ex) {
            log.warn("Audit service unavailable for productId={}: {}", id, ex.getMessage());
        }
    }
}
