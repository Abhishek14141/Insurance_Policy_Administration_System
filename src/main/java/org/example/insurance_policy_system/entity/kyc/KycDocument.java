
package org.example.insurance_policy_system.entity.kyc;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "kyc_document",
        indexes = @Index(
                name = "idx_kyc_document_number",
                columnList = "document_number"
        )
)
@Getter
@Setter
public class KycDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "kyc_id", nullable = false)
    private KycRequest kyc;

    @Column(name = "document_type", nullable = false, length = 40)
    private String documentType;

    @Column(name = "document_number", nullable = false, length = 100)
    private String documentNumber;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @Column(name = "verification_status", nullable = false, length = 20)
    private String verificationStatus = "Pending";

    @Column(length = 500)
    private String remarks;

    @PrePersist
    void onCreate() {
        if (uploadDate == null) {
            uploadDate = LocalDateTime.now();
        }
    }
}
