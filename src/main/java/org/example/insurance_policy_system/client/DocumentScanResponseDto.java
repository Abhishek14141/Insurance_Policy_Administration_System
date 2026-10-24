package org.example.insurance_policy_system.client;

import lombok.Data;

@Data
public class DocumentScanResponseDto {
    private String scanStatus; // CLEAN, INFECTED, PENDING, ERROR
    private String scanReference;
}
