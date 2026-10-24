package org.example.insurance_policy_system.controller.claimmanagement;

import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimResponseDto;
import org.example.insurance_policy_system.service.claimmanagement.ClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping("/register")
    public ResponseEntity<?> registerClaim(
            @RequestBody ClaimRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(claimService.registerClaim(request));
    }




    // 2. GET ALL CLAIMS
    // =========================================

    @GetMapping
    public ResponseEntity<List<ClaimResponseDto>> getAllClaims() {

        List<ClaimResponseDto> claims =
                claimService.getAllClaims();

        return ResponseEntity.ok(claims);
    }

    // =========================================
    // 3. GET CLAIM BY ID
    // =========================================

    @GetMapping("/{claimId}")
    public ResponseEntity<ClaimResponseDto> getClaimById(
            @PathVariable("claimId") Long claimId) {

        ClaimResponseDto response =
                claimService.getClaimById(claimId);

        return ResponseEntity.ok(response);
    }

    // =========================================
    // 4. UPDATE CLAIM
    // =========================================

    @PutMapping("/{claimId}")
    public ResponseEntity<ClaimResponseDto> updateClaim(
            @PathVariable("claimId") Long claimId,
            @RequestBody ClaimRequestDto request) {

        ClaimResponseDto response =
                claimService.updateClaim(claimId, request);

        return ResponseEntity.ok(response);
    }

    // =========================================
    // 5. CANCEL CLAIM
    // =========================================

    @PatchMapping("/{claimId}/cancel")
    public ResponseEntity<Void> cancelClaim(
            @PathVariable("claimId") Long claimId) {

        claimService.cancelClaim(claimId);

        return ResponseEntity.noContent().build();
    }
}