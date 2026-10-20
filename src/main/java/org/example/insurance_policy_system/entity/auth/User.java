package org.example.insurance_policy_system.entity.auth;

import jakarta.persistence.*;

import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.auth.UserStatus;

@Entity
@Table(name = "user_master")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // References the employee record owned by the relevant HR module.
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String mobile;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "branch_id")
    private Long branchId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status = UserStatus.Active;
}
