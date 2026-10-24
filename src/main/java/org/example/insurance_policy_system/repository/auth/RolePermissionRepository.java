package org.example.insurance_policy_system.repository.auth;


import org.example.insurance_policy_system.entity.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRole_IdAndPermission_Id(Long roleId, Long permissionId);
}
