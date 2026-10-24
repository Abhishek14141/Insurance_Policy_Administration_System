package org.example.insurance_policy_system.entity.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_contact")
@Data
@NoArgsConstructor
public class CustomerContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @Column(name = "alternate_mobile", length = 15)
    private String alternateMobile;

    @Column(name = "office_phone", length = 20)
    private String officePhone;

    @Column(name = "preferred_contact_mode", nullable = false, length = 20)
    private String preferredContactMode;
}
