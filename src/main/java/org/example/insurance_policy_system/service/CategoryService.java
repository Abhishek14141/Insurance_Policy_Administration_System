package org.example.insurance_policy_system.service;

import org.example.insurance_policy_system.dto.*;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto create(CategoryRequestDto request);

    List<CategoryResponseDto> getAll();

    CategoryResponseDto update(Long id, CategoryRequestDto request);

    void deactivate(Long id);
}
