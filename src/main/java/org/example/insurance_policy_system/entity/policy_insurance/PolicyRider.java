package org.example.insurance_policy_system.entity.policy_insurance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyRiderId;

    private Long policyId;

    private Long riderId;

    private BigDecimal riderPremium;



}
