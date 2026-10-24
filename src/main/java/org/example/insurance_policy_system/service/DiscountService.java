package org.example.insurance_policy_system.service;

import org.example.insurance_policy_system.dto.*;

import java.util.List;

public interface DiscountService {
    DiscountMasterResponseDto create(DiscountMasterRequestDto request);

    List<DiscountMasterResponseDto> getAll();

    DiscountMasterResponseDto update(Long id, DiscountMasterRequestDto request);

    DiscountMasterResponseDto updateStatus(Long id, String status);
}
