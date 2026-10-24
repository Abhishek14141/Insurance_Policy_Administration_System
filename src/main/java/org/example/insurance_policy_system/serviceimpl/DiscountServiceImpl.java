package org.example.insurance_policy_system.serviceimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.insurance_policy_system.client.AuditClient;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.*;
import org.example.insurance_policy_system.enums.DiscountStatus;
import org.example.insurance_policy_system.repository.DiscountMasterRepository;
import org.example.insurance_policy_system.service.DiscountService;
import org.example.insurance_policy_system.exception.BusinessException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiscountServiceImpl implements DiscountService {
    private final DiscountMasterRepository repo;
    private final ModelMapper mapper;
    private final AuditClient auditClient;

    public DiscountMasterResponseDto create(DiscountMasterRequestDto r) {
        validate(r);
        DiscountMaster e = mapper.map(r, DiscountMaster.class);
        e.setStatus(DiscountStatus.ACTIVE);
        e = repo.save(e);
        audit(e.getDiscountId(), "CREATE");
        return mapper.map(e, DiscountMasterResponseDto.class);
    }

    @Transactional(readOnly = true)
    public List<DiscountMasterResponseDto> getAll() {
        return repo.findAll().stream().map(e -> mapper.map(e, DiscountMasterResponseDto.class)).toList();
    }

    public DiscountMasterResponseDto update(Long id, DiscountMasterRequestDto r) {
        validate(r);
        DiscountMaster e = repo.findById(id).orElseThrow(() -> new BusinessException("Discount not found: " + id));
        mapper.map(r, e);
        e = repo.save(e);
        audit(id, "UPDATE");
        return mapper.map(e, DiscountMasterResponseDto.class);
    }

    public DiscountMasterResponseDto updateStatus(Long id, String status) {
        DiscountMaster e = repo.findById(id).orElseThrow(() -> new BusinessException("Discount not found: " + id));
        try {
            e.setStatus(DiscountStatus.valueOf(status.toUpperCase()));
        } catch (Exception ex) {
            throw new BusinessException("Discount status must be ACTIVE or INACTIVE");
        }
        e = repo.save(e);
        audit(id, "STATUS_UPDATE");
        return mapper.map(e, DiscountMasterResponseDto.class);
    }

    private void validate(DiscountMasterRequestDto r) {
        if (r.getValidFrom() != null && r.getValidTo() != null && r.getValidFrom().isAfter(r.getValidTo()))
            throw new BusinessException("Discount validFrom cannot be after validTo");
        if (r.getDiscountPercentage().compareTo(new java.math.BigDecimal("100")) > 0)
            throw new BusinessException("Discount cannot exceed 100 percent");
    }

    private void audit(Long id, String action) {
        try {
            auditClient.writeAuditLog(new AuditLogRequestDto("QUOTE", "DISCOUNT", id, action, null, "Discount master changed"));
        } catch (Exception ex) {
            log.warn("Audit service unavailable for discountId={}: {}", id, ex.getMessage());
        }
    }
}
