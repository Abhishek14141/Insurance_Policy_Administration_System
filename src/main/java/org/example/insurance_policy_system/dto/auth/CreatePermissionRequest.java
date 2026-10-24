package org.example.insurance_policy_system.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CreatePermissionRequest {
    @NotBlank private String permissionName;
    private String moduleName;
    private String actionType;

    public CreatePermissionRequest() {
    }

    public CreatePermissionRequest(String permissionName, String moduleName, String actionType) {
        this.permissionName = permissionName;
        this.moduleName = moduleName;
        this.actionType = actionType;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String permissionName() {
        return permissionName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String moduleName() {
        return moduleName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String actionType() {
        return actionType;
    }

}
