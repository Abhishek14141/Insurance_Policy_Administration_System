package org.example.insurance_policy_system.client;

import org.example.insurance_policy_system.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping("/api/customers/{id}")
    CustomerResponse getById(@PathVariable("id") Long id);
}
