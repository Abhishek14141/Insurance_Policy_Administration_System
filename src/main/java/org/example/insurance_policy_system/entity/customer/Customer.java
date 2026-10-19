package org.example.insurance_policy_system.entity.customer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.customer.CustomerStatus;
import org.example.insurance_policy_system.enums.customer.Gender;
import org.example.insurance_policy_system.enums.customer.MaritalStatus;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_code", length = 20, unique = true)
    private String customerCode;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false, length = 20)
    private MaritalStatus maritalStatus;

    @Column(nullable = false, length = 100)
    private String occupation;

    @Column(name = "annual_income", nullable = false,
            precision = 15, scale = 2)
    private BigDecimal annualIncome;

    @Column(name = "mobile_number", nullable = false,
            unique = true, length = 15)
    private String mobileNumber;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_status", nullable = false, length = 20)
    private CustomerStatus customerStatus = CustomerStatus.Active;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforeInsert() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void beforeUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
