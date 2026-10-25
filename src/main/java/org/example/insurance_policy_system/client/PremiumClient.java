package org.example.insurance_policy_system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@FeignClient(name = "premium-service", url = "${services.premium.url}")
public interface PremiumClient {

    @GetMapping("/internal/premiums/policies/{policyId}/claim-eligibility")
    PremiumEligibilityDto checkPremiumEligibility(
            @PathVariable("policyId") Long policyId,
            @RequestParam("incidentDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate incidentDate);
}