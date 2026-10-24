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
public class RiderController {
    private final ProductMasterService service;

    @PostMapping("/products/{id}/riders")
    public ResponseEntity<ApiResponse<RiderResponseDto>> add(@PathVariable Long id, @Valid @RequestBody RiderRequestDto dto) {
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Rider created", service.addRider(id, dto), LocalDateTime.now()));
    }

    @GetMapping("/products/{id}/riders")
    public ApiResponse<List<RiderResponseDto>> all(@PathVariable Long id) {
        return new ApiResponse<>(true, "Riders fetched", service.getRiders(id), LocalDateTime.now());
    }

    @PutMapping("/riders/{riderId}")
    public ApiResponse<RiderResponseDto> update(@PathVariable Long riderId, @Valid @RequestBody RiderRequestDto dto) {
        return new ApiResponse<>(true, "Rider updated", service.updateRider(riderId, dto), LocalDateTime.now());
    }

    @DeleteMapping("/riders/{riderId}")
    public ApiResponse<Void> delete(@PathVariable Long riderId) {
        service.deleteRider(riderId);
        return new ApiResponse<>(true, "Rider deleted", null, LocalDateTime.now());
    }
}
