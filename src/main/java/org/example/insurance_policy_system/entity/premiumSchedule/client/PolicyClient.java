package org.example.insurance_policy_system.entity.premiumSchedule.client;

import com.ampta.premiumservice.dto.PolicyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "policy-service", fallback = PolicyClientFallback.class)
public interface PolicyClient {

    @GetMapping("/api/v1/policies/{id}")
    PolicyDto getPolicy(@PathVariable("id") Long id);

}
