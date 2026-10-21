package org.example.insurance_policy_system.client;

import org.example.insurance_policy_system.dto.undertakingDto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "customer-service-client", url = "${services.customer.url:http://localhost:8081}")
public interface CustomerClient {
    @GetMapping("/api/customers/{id}")
    CustomerResponse getCustomer(@PathVariable("id") Long id);
}
