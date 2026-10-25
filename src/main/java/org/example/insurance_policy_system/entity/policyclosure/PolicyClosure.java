package org.example.insurance_policy_system.entity.policyclosure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "policy_closure",
        uniqueConstraints = @UniqueConstraint(name = "uk_policy_closure_number", columnNames = "closure_number"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyClosure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long closureId;

    @NotNull @Positive @Column(name="policy_id", nullable=false)
    private Long policyId;

    @Column(name="closure_number", nullable=false, unique=true, length=40)
    private String closureNumber;

    @Enumerated(EnumType.STRING) @Column(name="closure_type", nullable=false, length=30)
    private ClosureType closureType;

    @Enumerated(EnumType.STRING) @Column(name="closure_status", nullable=false, length=20)
    private ClosureStatus closureStatus;

    @Column(name="policy_expiry_date", nullable=false)
    private LocalDate policyExpiryDate;

    @Positive @Column(name="closed_by")
    private Long closedBy;

    @Column(name="closure_date", nullable=false, updatable=false)
    private LocalDateTime closureDate;

    @Size(max=1000) @Column(length=1000)
    private String remarks;

    @PrePersist protected void onCreate() {
        if (closureDate == null) closureDate = LocalDateTime.now(); }
}
