package org.example.insurance_policy_system.repository.auth;

import java.util.List;

import org.example.insurance_policy_system.entity.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole_RoleId(Long roleId);

    boolean existsByRole_RoleIdAndPermission_PermissionId(
            Long roleId, Long permissionId);
}
