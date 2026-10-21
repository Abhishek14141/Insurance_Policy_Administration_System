package org.example.insurance_policy_system.repository.auth;

import java.util.Optional;

import org.example.insurance_policy_system.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);
}
