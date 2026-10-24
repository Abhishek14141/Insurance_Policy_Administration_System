package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.ProductMasterService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BenefitController {
    private final ProductMasterService service;

    @PostMapping("/products/{id}/benefits")
    public ResponseEntity<ApiResponse<BenefitResponseDto>> add(@PathVariable Long id, @Valid @RequestBody BenefitRequestDto dto) {
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Benefit created", service.addBenefit(id, dto), LocalDateTime.now()));
    }

    @GetMapping("/products/{id}/benefits")
    public ApiResponse<List<BenefitResponseDto>> all(@PathVariable Long id) {
        return new ApiResponse<>(true, "Benefits fetched", service.getBenefits(id), LocalDateTime.now());
    }

    @PutMapping("/benefits/{benefitId}")
    public ApiResponse<BenefitResponseDto> update(@PathVariable Long benefitId, @Valid @RequestBody BenefitRequestDto dto) {
        return new ApiResponse<>(true, "Benefit updated", service.updateBenefit(benefitId, dto), LocalDateTime.now());
    }

    @DeleteMapping("/benefits/{benefitId}")
    public ApiResponse<Void> delete(@PathVariable Long benefitId) {
        service.deleteBenefit(benefitId);
        return new ApiResponse<>(true, "Benefit deleted", null, LocalDateTime.now());
    }
}
