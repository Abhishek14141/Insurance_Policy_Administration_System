package org.example.insurance_policy_system.entity.premiumSchedule.client;

import com.ampta.premiumservice.dto.PolicyDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PolicyClientFallback implements PolicyClient {

    @Override
    public PolicyDto getPolicy(Long id) {
        return
                new PolicyDto(
                        id,
                        "HLT-MH-2026-100245",
                        1L,
                        LocalDate.now(),
                        LocalDate.now().plusYears(1),
                        "YEARLY",
                        BigDecimal.valueOf(3717)
                );
    }
}
