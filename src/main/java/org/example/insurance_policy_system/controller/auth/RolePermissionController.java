package org.example.insurance_policy_system.controller.auth;

import jakarta.validation.Valid;

import org.example.authservice.util.ApiResponse;
import org.example.insurance_policy_system.dto.auth.AssignPermissionsRequest;
import org.example.insurance_policy_system.dto.auth.CreatePermissionRequest;
import org.example.insurance_policy_system.dto.auth.CreateRoleRequest;
import org.example.insurance_policy_system.entity.auth.Permission;
import org.example.insurance_policy_system.entity.auth.Role;
import org.example.insurance_policy_system.service.auth.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RolePermissionController {

    private final AdminService service;

    public RolePermissionController(AdminService service) {
        this.service = service;
    }

    @PostMapping("/api/v1/roles")
    public ApiResponse<Role> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ApiResponse.ok("Role created", service.createRole(request));
    }

    @GetMapping("/api/v1/roles")
    public ApiResponse<?> roles() {
        return ApiResponse.ok("Roles fetched", service.roles());
    }

    @PostMapping("/api/v1/permissions")
    public ApiResponse<Permission> createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        return ApiResponse.ok("Permission created", service.createPermission(request));
    }

    @GetMapping("/api/v1/permissions")
    public ApiResponse<?> permissions() {
        return ApiResponse.ok("Permissions fetched", service.permissions());
    }

    @PutMapping("/api/v1/roles/{id}/permissions")
    public ApiResponse<Void> assign(@PathVariable Long id, @Valid @RequestBody AssignPermissionsRequest request) {
        service.assign(id, request.permissionIds());
        return ApiResponse.ok("Permissions assigned", null);
    }
}
