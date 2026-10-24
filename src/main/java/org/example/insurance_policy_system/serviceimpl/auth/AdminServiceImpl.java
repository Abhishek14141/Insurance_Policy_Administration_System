package org.example.insurance_policy_system.serviceimpl.auth;


import org.example.insurance_policy_system.dto.auth.CreatePermissionRequest;
import org.example.insurance_policy_system.dto.auth.CreateRoleRequest;
import org.example.insurance_policy_system.dto.auth.UserUpdateRequest;
import org.example.insurance_policy_system.entity.auth.Permission;
import org.example.insurance_policy_system.entity.auth.Role;
import org.example.insurance_policy_system.entity.auth.RolePermission;
import org.example.insurance_policy_system.entity.auth.User;
import org.example.insurance_policy_system.enums.auth.UserStatus;
import org.example.insurance_policy_system.repository.auth.PermissionRepository;
import org.example.insurance_policy_system.repository.auth.RolePermissionRepository;
import org.example.insurance_policy_system.repository.auth.RoleRepository;
import org.example.insurance_policy_system.repository.auth.UserRepository;
import org.example.insurance_policy_system.service.auth.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PermissionRepository permissions;
    private final RolePermissionRepository mappings;
    private final PasswordEncoder encoder;

    public AdminServiceImpl(
            UserRepository users,
            RoleRepository roles,
            PermissionRepository permissions,
            RolePermissionRepository mappings,
            PasswordEncoder encoder) {

        this.users = users;
        this.roles = roles;
        this.permissions = permissions;
        this.mappings = mappings;
        this.encoder = encoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> users() {
        return users.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User user(Long id) {
        return users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    @Override
    public User update(Long id, UserUpdateRequest request) {
        User user = user(id);

        if (request.roleName() != null) {
            user.setRole(
                    roles.findByRoleNameIgnoreCase(request.roleName())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Role not found"
                            ))
            );
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.mobile() != null) {
            user.setMobile(request.mobile());
        }

        if (request.status() != null) {
            try {
                user.setStatus(UserStatus.valueOf(request.status()));
            } catch (IllegalArgumentException exception) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid user status"
                );
            }
        }

        return users.save(user);
    }

    @Override
    public void delete(Long id) {
        users.delete(user(id));
    }

    @Override
    public User lock(Long id) {
        User user = user(id);
        user.setStatus(UserStatus.Locked);
        return users.save(user);
    }

    @Override
    public User unlock(Long id) {
        User user = user(id);
        user.setStatus(UserStatus.Active);
        return users.save(user);
    }

    @Override
    public Role createRole(CreateRoleRequest request) {
        Role role = new Role();
        role.setRoleName(request.roleName().toUpperCase());
        role.setDescription(request.description());

        return roles.save(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> roles() {
        return roles.findAll();
    }

    @Override
    public Permission createPermission(CreatePermissionRequest request) {
        Permission permission = new Permission();
        permission.setPermissionName(request.permissionName());
        permission.setModuleName(request.moduleName());
        permission.setActionType(request.actionType());

        return permissions.save(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> permissions() {
        return permissions.findAll();
    }

    @Override
    public void assign(Long roleId, List<Long> permissionIds) {
        Role role = roles.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found"
                ));

        for (Long permissionId : permissionIds.stream().distinct().toList()) {

            Permission permission = permissions.findById(permissionId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Permission not found"
                    ));

            boolean alreadyAssigned =
                    mappings.existsByRole_IdAndPermission_Id(
                            roleId,
                            permissionId
                    );

            if (!alreadyAssigned) {
                RolePermission mapping = new RolePermission();
                mapping.setRole(role);
                mapping.setPermission(permission);
                mappings.save(mapping);
            }
        }
    }
}

