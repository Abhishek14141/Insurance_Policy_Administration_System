package org.example.insurance_policy_system.client;


import org.example.insurance_policy_system.dto.claimsettlement.ClaimResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ClaimClient {


    @GetMapping("/api/v1/claims/{id}")
    ClaimResponseDto getClaimById(@PathVariable("id") Long claimId);

}