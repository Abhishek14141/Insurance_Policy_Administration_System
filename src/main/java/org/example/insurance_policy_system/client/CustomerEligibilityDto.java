package org.example.insurance_policy_system.client;

import lombok.Data;

@Data
public class CustomerEligibilityDto {

    private Long customerId;
    private Boolean active;
}