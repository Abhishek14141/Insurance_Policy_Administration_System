package org.example.insurance_policy_system.client;

import lombok.Data;

@Data
public class ClaimNotificationDto {

    private Long claimId;
    private Long customerId;
    private String claimNumber;

    private String eventType;
    private String message;
}