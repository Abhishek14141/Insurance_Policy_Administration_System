package org.example.insurance_policy_system.serviceimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.insurance_policy_system.client.AuditClient;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.*;
import org.example.insurance_policy_system.enums.CategoryStatus;
import org.example.insurance_policy_system.repository.ProductCategoryRepository;
import org.example.insurance_policy_system.service.CategoryService;
import org.example.insurance_policy_system.exception.BusinessException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final ProductCategoryRepository repository;
    private final ModelMapper mapper;
    private final AuditClient auditClient;

    public CategoryResponseDto create(CategoryRequestDto r) {
        if (repository.existsByCategoryNameIgnoreCase(r.getCategoryName()))
            throw new BusinessException("Category already exists");
        ProductCategory e = repository.save(mapper.map(r, ProductCategory.class));
        audit(e.getCategoryId(), "CREATE");
        return dto(e);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAll() {
        return repository.findAll().stream().map(this::dto).toList();
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto r) {
        ProductCategory e = repository.findById(id).orElseThrow(() -> new BusinessException("Product category not found: " + id));
        if (!e.getCategoryName().equalsIgnoreCase(r.getCategoryName()) && repository.existsByCategoryNameIgnoreCase(r.getCategoryName()))
            throw new BusinessException("Category already exists");
        e.setCategoryName(r.getCategoryName());
        e.setDescription(r.getDescription());
        e = repository.save(e);
        audit(e.getCategoryId(), "UPDATE");
        return dto(e);
    }

    public void deactivate(Long id) {
        ProductCategory e = repository.findById(id).orElseThrow(() -> new BusinessException("Product category not found: " + id));
        e.setStatus(CategoryStatus.INACTIVE);
        repository.save(e);
        audit(id, "DEACTIVATE");
    }

    private CategoryResponseDto dto(ProductCategory e) {
        CategoryResponseDto d = mapper.map(e, CategoryResponseDto.class);
        d.setStatus(e.getStatus().name());
        return d;
    }

    private void audit(Long id, String action) {
        try {
            auditClient.writeAuditLog(new AuditLogRequestDto("PRODUCT", "PRODUCT_CATEGORY", id, action, null, "Product category action"));
        } catch (Exception ex) {
            log.warn("Audit service unavailable for categoryId={}: {}", id, ex.getMessage());
        }
    }
}
