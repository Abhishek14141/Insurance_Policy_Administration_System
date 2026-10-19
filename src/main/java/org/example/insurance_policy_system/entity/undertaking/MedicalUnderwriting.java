package org.example.insurance_policy_system.entity.undertaking;

import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity
@Table(name = "medical_underwriting")
public class MedicalUnderwriting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_id")
    private Long medicalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "underwriting_id", nullable = false)
    private UnderwritingCase underwritingCase;

    @Column(name = "height", precision = 5, scale = 2)
    private BigDecimal height;

    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "bmi", precision = 5, scale = 2)
    private BigDecimal bmi;

    @Column(name = "smoking")
    private Boolean smoking;

    @Column(name = "alcohol")
    private Boolean alcohol;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "medical_report_path", length = 500)
    private String medicalReportPath;
}