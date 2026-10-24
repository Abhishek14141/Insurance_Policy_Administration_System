package org.example.insurance_policy_system.controller.claimmanagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimmanagement.*;
import org.example.insurance_policy_system.service.claimmanagement.BankVerificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/claims/{claimId}")
public class BankVerificationController {

    private final BankVerificationService service;

    @PostMapping("/bank-details")
    public BankDetailsResponseDto save(
            @PathVariable Long claimId,
            @Valid @RequestBody BankDetailsRequestDto request) {

        return service.save(claimId, request);
    }

    @PostMapping("/bank/verify")
    public BankDetailsResponseDto verify(@PathVariable Long claimId) {

        return service.demoVerify(claimId);
    }
}