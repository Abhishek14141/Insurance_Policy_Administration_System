package org.example.insurance_policy_system.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class KycResponse {
    private Long id;
    private Long customerId;
    private String kycNumber;
    private LocalDate kycDate;
    private String status;
    private String remarks;
    private String panStatus;
    private String ckycStatus;
    private LocalDateTime verifiedDate;
}
