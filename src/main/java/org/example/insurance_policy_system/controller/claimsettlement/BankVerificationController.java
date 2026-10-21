package org.example.insurance_policy_system.controller.claimsettlement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.claimsettlement.BankVerificationRequestDto;
import org.example.insurance_policy_system.dto.claimsettlement.BankVerificationResponseDto;
import org.example.insurance_policy_system.service.claimsettlement.BankVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class BankVerificationController {
//
//    private final BankVerificationService bankVerificationService;
//
//    @PostMapping("/{claimId}/bank-verification")
//    public ResponseEntity<BankVerificationResponseDto> verify(
//            @PathVariable Long claimId,
//            @Valid @RequestBody BankVerificationRequestDto request) {
//
//        return ResponseEntity.ok(
//                bankVerificationService.verifyBankAccount(
//                        claimId, request
//                )
//        );
//    }
}