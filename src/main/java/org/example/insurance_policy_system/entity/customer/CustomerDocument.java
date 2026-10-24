package org.example.insurance_policy_system.entity.customer;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.customer.DocumentVerificationStatus;

@Entity
@Table(name = "customer_document")
@Data
@NoArgsConstructor
public class CustomerDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "document_type", nullable = false, length = 100)
    private String documentType;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "uploaded_date")
    private LocalDateTime uploadedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", length = 20)
    private DocumentVerificationStatus verificationStatus =
            DocumentVerificationStatus.Pending;

    @PrePersist
    public void beforeInsert() {
        if (uploadedDate == null) {
            uploadedDate = LocalDateTime.now();
        }
    }
}
