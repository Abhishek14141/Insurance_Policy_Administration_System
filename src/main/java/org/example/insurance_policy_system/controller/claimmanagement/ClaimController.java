package org.example.insurance_policy_system.controller.claimmanagement;

import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimRequestDto;
import org.example.insurance_policy_system.service.claimmanagement.ClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}