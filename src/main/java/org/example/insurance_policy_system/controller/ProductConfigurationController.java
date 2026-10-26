package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.ProductConfigurationService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductConfigurationController {
    private final ProductConfigurationService service;

    @PostMapping("/premium-frequencies")
    public ResponseEntity<ApiResponse<PremiumFrequencyResponseDto>> createFrequency(@Valid @RequestBody PremiumFrequencyRequestDto r) {
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Premium frequency created", service.createFrequency(r), LocalDateTime.now()));
    }

    @GetMapping("/premium-frequencies")
    public ApiResponse<List<PremiumFrequencyResponseDto>> frequencies() {
        return new ApiResponse<>(true, "Premium frequencies fetched", service.getFrequencies(), LocalDateTime.now());
    }

    @PutMapping("/products/{id}/commission")
    public ApiResponse<ProductCommissionResponseDto> saveCommission(@PathVariable Long id, @Valid @RequestBody ProductCommissionRequestDto r) {
        return new ApiResponse<>(true, "Commission configuration saved", service.saveCommission(id, r), LocalDateTime.now());
    }

    @GetMapping("/products/{id}/commission")
    public ApiResponse<ProductCommissionResponseDto> getCommission(@PathVariable Long id) {
        return new ApiResponse<>(true, "Commission configuration fetched", service.getCommission(id), LocalDateTime.now());
    }
}
