package org.example.insurance_policy_system.entity.auth;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permission_master")
@Data
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "permission_name", nullable = false, length = 100)
    private String permissionName;

    @Column(name = "module_name", length = 100)
    private String moduleName;

    @Column(name = "action_type", length = 50)
    private String actionType;
}
