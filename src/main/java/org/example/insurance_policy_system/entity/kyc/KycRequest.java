
package org.example.insurance_policy_system.entity.kyc;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import org.example.insurance_policy_system.entity.customer.Customer;

@Entity
@Table(
        name = "customer_kyc",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_customer_kyc_number",
                columnNames = "kyc_number"
        )
)
@Getter
@Setter
public class KycRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kyc_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "kyc_number", nullable = false, unique = true, length = 30)
    private String kycNumber;

    @Column(name = "kyc_date", nullable = false)
    private LocalDate kycDate;

    @Column(name = "verification_status", nullable = false, length = 20)
    private String status = "Pending";

    @Column(length = 500)
    private String remarks;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @Column(name = "aadhaar_number", length = 12)
    private String aadhaarNumber;

    @Column(name = "aadhaar_name", length = 150)
    private String aadhaarName;

    @Column(name = "aadhaar_dob")
    private LocalDate aadhaarDateOfBirth;

    @Column(name = "aadhaar_address", length = 500)
    private String aadhaarAddress;

    @Column(name = "pan_number", length = 10)
    private String panNumber;

    @Column(name = "pan_holder_name", length = 150)
    private String panHolderName;

    @Column(name = "pan_status", length = 20)
    private String panStatus = "Pending";

    @Column(name = "ckyc_number", length = 30)
    private String ckycNumber;

    @Column(name = "ckyc_status", length = 20)
    private String ckycStatus = "Pending";

    @Column(name = "ckyc_verification_date")
    private LocalDateTime ckycVerificationDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (kycDate == null) {
            kycDate = LocalDate.now();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
