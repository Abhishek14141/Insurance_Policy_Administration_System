package org.example.insurance_policy_system.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class AssignPermissionsRequest {
    @NotEmpty private List<Long> permissionIds;

    public AssignPermissionsRequest() {
    }

    public AssignPermissionsRequest(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public List<Long> permissionIds() {
        return permissionIds;
    }

}
