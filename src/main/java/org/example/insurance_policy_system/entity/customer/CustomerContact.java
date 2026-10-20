package org.example.insurance_policy_system.entity.customer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.customer.PreferredContactMode;

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
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @Column(name = "alternate_mobile", length = 15)
    private String alternateMobile;

    @Column(name = "office_phone", length = 20)
    private String officePhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_contact_mode", length = 20)
    private PreferredContactMode preferredContactMode = PreferredContactMode.Mobile;
}
