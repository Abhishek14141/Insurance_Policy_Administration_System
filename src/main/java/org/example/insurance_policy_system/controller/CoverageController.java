package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.ProductMasterService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class CoverageController {
    private final ProductMasterService service;

    @PostMapping("/products/{id}/coverages")
    public ResponseEntity<ApiResponse<CoverageResponseDto>> add(@PathVariable Long id, @Valid @RequestBody CoverageRequestDto dto) {
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Coverage created", service.addCoverage(id, dto), LocalDateTime.now()));
    }

    @GetMapping("/products/{id}/coverages")
    public ApiResponse<List<CoverageResponseDto>> all(@PathVariable Long id) {
        return new ApiResponse<>(true, "Coverages fetched", service.getCoverages(id), LocalDateTime.now());
    }

    @PutMapping("/coverages/{coverageId}")
    public ApiResponse<CoverageResponseDto> update(@PathVariable Long coverageId, @Valid @RequestBody CoverageRequestDto dto) {
        return new ApiResponse<>(true, "Coverage updated", service.updateCoverage(coverageId, dto), LocalDateTime.now());
    }

    @DeleteMapping("/coverages/{coverageId}")
    public ApiResponse<Void> delete(@PathVariable Long coverageId) {
        service.deleteCoverage(coverageId);
        return new ApiResponse<>(true, "Coverage deleted", null, LocalDateTime.now());
    }
}
