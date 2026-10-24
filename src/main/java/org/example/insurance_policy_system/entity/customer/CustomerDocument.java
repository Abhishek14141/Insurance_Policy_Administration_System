package org.example.insurance_policy_system.entity.customer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity @Table(name = "customer_document") @Data @NoArgsConstructor
public class CustomerDocument {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "document_id") private Long documentId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "customer_id", nullable = false) private Customer customer;
    @Column(name = "document_type", nullable = false, length = 100) private String documentType;
    @Column(name = "file_name", nullable = false, length = 255) private String fileName;
    @Column(name = "file_path", nullable = false, length = 500) private String filePath;
    @Column(name = "uploaded_date", nullable = false, updatable = false) private LocalDateTime uploadedDate;
    @Column(name = "verification_status", nullable = false, length = 20) private String verificationStatus = "Pending";
    @PrePersist public void onUpload() { uploadedDate = LocalDateTime.now(); if (verificationStatus == null) verificationStatus = "Pending"; }
}
