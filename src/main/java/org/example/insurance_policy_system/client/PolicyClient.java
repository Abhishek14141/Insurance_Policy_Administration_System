package org.example.insurance_policy_system.client;

//import org.example.insurance_policy_system.dto.claimmanagement.client.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@FeignClient(name = "policy-service", url = "${services.policy.url}")
public interface PolicyClient {

    @GetMapping("/internal/policies/{policyId}/claim-eligibility")
    PolicyEligibilityDto checkClaimEligibility(
            @PathVariable("policyId") Long policyId,
            @RequestParam("customerId") Long customerId,
            @RequestParam("claimType") String claimType,
            @RequestParam("incidentDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate incidentDate);

    @GetMapping("/internal/policies/{policyId}/claim-coverage")
    PolicyCoverageDto getClaimCoverage(
            @PathVariable("policyId") Long policyId,
            @RequestParam("claimType") String claimType);
}