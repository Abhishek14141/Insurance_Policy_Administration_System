package org.example.insurance_policy_system.entity.customer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer", uniqueConstraints = {
    @UniqueConstraint(name="uk_customer_mobile", columnNames="mobile_number"),
    @UniqueConstraint(name="uk_customer_email", columnNames="email")
})
@Data @NoArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="customer_id")
    private Long customerId;
    @Column(name="customer_code", unique=true, nullable=false) private String customerCode;
    @Column(name="first_name", nullable=false) private String firstName;
    @Column(name="middle_name") private String middleName;
    @Column(name="last_name", nullable=false) private String lastName;
    @Column(name="date_of_birth", nullable=false) private LocalDate dateOfBirth;
    @Column(nullable=false) private String gender;
    @Column(name="marital_status", nullable=false) private String maritalStatus;
    @Column(name="mobile_number", nullable=false, unique=true, length=10) private String mobileNumber;
    @Column(nullable=false, unique=true) private String email;
    @Column(name="customer_status", nullable=false) private String customerStatus="ACTIVE";
    @Column(name="created_at", nullable=false, updatable=false) private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private CustomerContact contact;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private CustomerOccupation occupationDetails;
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL, orphanRemoval=true) private List<CustomerAddress> addresses=new ArrayList<>();
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL, orphanRemoval=true) private List<Nominee> nominees=new ArrayList<>();
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL, orphanRemoval=true) private List<CustomerDocument> documents=new ArrayList<>();
    @PrePersist public void onCreate(){ createdAt=LocalDateTime.now(); }
    @PreUpdate public void onUpdate(){ updatedAt=LocalDateTime.now(); }
}
