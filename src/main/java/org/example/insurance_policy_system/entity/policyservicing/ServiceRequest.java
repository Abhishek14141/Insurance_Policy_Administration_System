package org.example.insurance_policy_system.entity.policyservicing;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.insurance_policy_system.entity.customer.Customer;
import org.example.insurance_policy_system.entity.policy_insurance.Policy;
import org.example.insurance_policy_system.enums.policyservicing.ServiceRequestStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "service_request",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_service_request_number",
                        columnNames = "request_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "request_number", nullable = false, unique = true)
    private String requestNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "request_type", nullable = false)
    private String requestType;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ServiceRequestStatus status = ServiceRequestStatus.SUBMITTED;

    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL)
    private List<PolicyEndorsement> endorsements = new ArrayList<>();

    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL)
    private List<ServiceRequestHistory> history = new ArrayList<>();
}
