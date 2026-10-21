package org.example.insurance_policy_system.entity.policyservicing;




import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.insurance_policy_system.entity.policy_insurance.Policy;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "nominee_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NomineeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nominee_id")
    private Long nomineeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "nominee_name", nullable = false)
    private String nomineeName;

    @Column(name = "relationship", nullable = false)
    private String relationship;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "share_percentage", precision = 5, scale = 2)
    private BigDecimal sharePercentage;

    @Column(name = "guardian_name")
    private String guardianName;
}
