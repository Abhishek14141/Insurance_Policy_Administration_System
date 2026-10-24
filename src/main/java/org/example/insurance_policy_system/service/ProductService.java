package org.example.insurance_policy_system.service;

import org.example.insurance_policy_system.dto.*;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto request);

    ProductResponseDto get(Long id);

    List<ProductResponseDto> getAll();

    ProductResponseDto update(Long id, ProductRequestDto request);

    ProductResponseDto updateStatus(Long id, String status);

    void deactivate(Long id);
}
