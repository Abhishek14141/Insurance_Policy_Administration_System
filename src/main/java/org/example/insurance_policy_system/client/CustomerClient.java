package org.example.insurance_policy_system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "customer-service-client", url = "${services.customer.url:http://localhost:8083}")
public interface CustomerClient {
    @GetMapping("/api/v1/customers/{id}")
    Map<String, Object> getCustomer(@PathVariable("id") Long id);
}
