package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.CategoryService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product-categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> create(@Valid @RequestBody CategoryRequestDto r) {
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Category created", service.create(r), LocalDateTime.now()));
    }

    @GetMapping
    public ApiResponse<List<CategoryResponseDto>> all() {
        return new ApiResponse<>(true, "Categories fetched", service.getAll(), LocalDateTime.now());
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponseDto> update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto r) {
        return new ApiResponse<>(true, "Category updated", service.update(id, r), LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.deactivate(id);
        return new ApiResponse<>(true, "Category deactivated", null, LocalDateTime.now());
    }
}
