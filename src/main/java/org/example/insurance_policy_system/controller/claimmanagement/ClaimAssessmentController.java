package org.example.insurance_policy_system.controller.claimmanagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimmanagement.*;
import org.example.insurance_policy_system.service.claimmanagement.ClaimAssessmentService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class ClaimAssessmentController {

    private final ClaimAssessmentService assessmentService;

    @PostMapping("/{claimId}/assessment")
    public ResponseEntity<ClaimAssessmentResponseDto> createAssessment(
            @PathVariable("claimId") Long claimId,
            @Valid @RequestBody ClaimAssessmentRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assessmentService.createAssessment(claimId, dto));
    }

    @GetMapping("/{claimId}/assessment")
    public ResponseEntity<ClaimAssessmentResponseDto> getAssessment(
            @PathVariable("claimId") Long claimId) {

        return ResponseEntity.ok(
                assessmentService.getAssessment(claimId)
        );
    }

    @PutMapping("/{claimId}/assessment")
    public ResponseEntity<ClaimAssessmentResponseDto> updateAssessment(
            @PathVariable("claimId") Long claimId,
            @Valid @RequestBody ClaimAssessmentRequestDto dto) {

        return ResponseEntity.ok(
                assessmentService.updateAssessment(claimId, dto));
    }
}