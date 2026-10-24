package org.example.insurance_policy_system.client;

import org.example.insurance_policy_system.dto.claimmanagement.ProductCoverageResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-coverage-client",
        url = "${services.product.url}"
)
public interface ProductCoverageClient {

    @GetMapping("/api/v1/products/coverages/{coverageId}")
    ProductCoverageResponseDto getCoverageById(
            @PathVariable("coverageId") Long coverageId
    );
}