package org.example.insurance_policy_system.controller.policyclosure;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.policyclosure.PolicyClosureResponseDto;
import org.example.insurance_policy_system.dto.policyclosure.PolicyExpiryCheckResponseDto;
import org.example.insurance_policy_system.service.policyclosure.PolicyClosureService;
import org.example.insurance_policy_system.util.ApiResponse;
import org.example.policyclosureservice.dto.PolicyClosureRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/policy-closures")
@RequiredArgsConstructor
@Validated
public class PolicyClosureController {

    private final PolicyClosureService policyClosureService;

    @GetMapping("/policy/{policyId}/check")
    public ResponseEntity<ApiResponse<PolicyExpiryCheckResponseDto>> checkPolicy(
            @PathVariable @Positive Long policyId) {
        return ResponseEntity.ok(ApiResponse.success("Policy expiry checked",
                policyClosureService.checkExpiry(policyId)));
    }

    @PostMapping("/policy/{policyId}/expire")
    public ResponseEntity<ApiResponse<PolicyClosureResponseDto>> expirePolicy(
            @PathVariable @Positive Long policyId,
            @Valid @RequestBody PolicyClosureRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Policy expired manually by admin",
                        policyClosureService.expirePolicy(policyId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PolicyClosureResponseDto>>> getAllClosures() {
        return ResponseEntity.ok(ApiResponse.success("Closure records fetched",
                policyClosureService.getAllClosures()));
    }

    @GetMapping("/{closureId}")
    public ResponseEntity<ApiResponse<PolicyClosureResponseDto>> getClosureById(
            @PathVariable @Positive Long closureId) {
        return ResponseEntity.ok(ApiResponse.success("Closure record fetched",
                policyClosureService.getClosureById(closureId)));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<PolicyClosureResponseDto>>> getClosuresByPolicyId(
            @PathVariable @Positive Long policyId) {
        return ResponseEntity.ok(ApiResponse.success("Policy closure history fetched",
                policyClosureService.getClosuresByPolicyId(policyId)));
    }
}
