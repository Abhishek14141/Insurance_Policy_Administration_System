package org.example.insurance_policy_system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customer-service", url = "${services.customer.url}")
public interface CustomerClient {

    @GetMapping("/internal/customers/{customerId}/eligibility")
    CustomerEligibilityDto getCustomerEligibility(
            @PathVariable("customerId") Long customerId);
}