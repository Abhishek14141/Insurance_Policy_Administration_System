package org.example.insurance_policy_system.service.auth;


import org.example.insurance_policy_system.dto.auth.CreatePermissionRequest;
import org.example.insurance_policy_system.dto.auth.CreateRoleRequest;
import org.example.insurance_policy_system.dto.auth.UserUpdateRequest;
import org.example.insurance_policy_system.entity.auth.Permission;
import org.example.insurance_policy_system.entity.auth.Role;
import org.example.insurance_policy_system.entity.auth.User;

import java.util.List;

public interface AdminService {

    List<User> users();

    User user(Long id);

    User update(Long id, UserUpdateRequest request);

    void delete(Long id);

    User lock(Long id);

    User unlock(Long id);

    Role createRole(CreateRoleRequest request);

    List<Role> roles();

    Permission createPermission(CreatePermissionRequest request);

    List<Permission> permissions();

    void assign(Long roleId, List<Long> permissionIds);
}
