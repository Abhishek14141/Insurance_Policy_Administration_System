package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.DiscountService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
@CrossOrigin(origins = "http://localhost:4200")
public class DiscountController {
    private final DiscountService service;

    @PostMapping
    public ResponseEntity<ApiResponse<DiscountMasterResponseDto>> create(@Valid @RequestBody DiscountMasterRequestDto r) {
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Discount created", service.create(r), LocalDateTime.now()));
    }

    @GetMapping
    public ApiResponse<List<DiscountMasterResponseDto>> all() {
        return new ApiResponse<>(true, "Discounts fetched", service.getAll(), LocalDateTime.now());
    }

    @PutMapping("/{id}")
    public ApiResponse<DiscountMasterResponseDto> update(@PathVariable Long id, @Valid @RequestBody DiscountMasterRequestDto r) {
        return new ApiResponse<>(true, "Discount updated", service.update(id, r), LocalDateTime.now());
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<DiscountMasterResponseDto> status(@PathVariable Long id, @RequestParam String value) {
        return new ApiResponse<>(true, "Discount status updated", service.updateStatus(id, value), LocalDateTime.now());
    }
}
