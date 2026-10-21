package org.example.insurance_policy_system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "product-service-client", url = "${services.product.url:http://localhost:8084}")
public interface ProductClient {
    @GetMapping("/api/v1/products/{id}")
    Map<String, Object> getProduct(@PathVariable("id") Long id);
}
