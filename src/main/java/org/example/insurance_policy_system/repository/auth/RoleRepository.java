package org.example.insurance_policy_system.repository.auth;
import org.example.insurance_policy_system.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleNameIgnoreCase(String roleName);
}
