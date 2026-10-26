package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.ProductService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> create(@Valid @RequestBody ProductRequestDto r) {
        log.info("Create product request");
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(true, "Product created successfully",
                        productService.create(r), LocalDateTime.now()));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDto> get(@PathVariable Long id) {
        return new ApiResponse<>(true, "Product fetched successfully",
                productService.get(id), LocalDateTime.now());
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDto>> all() {
        return new ApiResponse<>(true, "Products fetched successfully", productService.getAll(), LocalDateTime.now());
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto r) {
        return new ApiResponse<>(true, "Product updated successfully", productService.update(id, r), LocalDateTime.now());
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<ProductResponseDto> status(@PathVariable Long id, @RequestParam String value) {
        return new ApiResponse<>(true, "Product status updated successfully", productService.updateStatus(id, value), LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        productService.deactivate(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product deactivated successfully", null, LocalDateTime.now()));
    }
}
