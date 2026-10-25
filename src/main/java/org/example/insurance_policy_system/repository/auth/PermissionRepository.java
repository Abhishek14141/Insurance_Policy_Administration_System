package org.example.insurance_policy_system.repository.auth;
import org.example.insurance_policy_system.entity.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
