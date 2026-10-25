package org.example.insurance_policy_system.dto.customer;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponse {
    private Long documentId;
    private Long customerId;
    private String documentType;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadedDate;
    private String verificationStatus;
}
