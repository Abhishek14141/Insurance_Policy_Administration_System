package org.example.insurance_policy_system.client;

import org.example.insurance_policy_system.dto.insuranceManagement.ProductResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8083")
                .build();
    }

    public ProductResponse getProductById(Long productId) {

        return restClient.get()
                .uri("/api/v1/products/{id}", productId)
                .retrieve()
                .body(ProductResponse.class);
    }
}