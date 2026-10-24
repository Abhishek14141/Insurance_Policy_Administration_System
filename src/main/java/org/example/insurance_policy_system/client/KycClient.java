package org.example.insurance_policy_system.client;

import org.example.insurance_policy_system.dto.KycResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "kyc-service-client")
public interface KycClient {
    @GetMapping("/api/kyc/customer/{customerId}")
    KycResponse getKycByCustomerId(
            @PathVariable("customerId") Long customerId
    );
}