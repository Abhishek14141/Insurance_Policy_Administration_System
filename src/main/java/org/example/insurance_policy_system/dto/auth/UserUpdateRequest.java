package org.example.insurance_policy_system.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserUpdateRequest {
    @Email private String email;
    @Size(max=20) private String mobile;
    private String roleName;
    private String status;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String email, String mobile, String roleName, String status) {
        this.email = email;
        this.mobile = mobile;
        this.roleName = roleName;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String email() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String mobile() {
        return mobile;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String roleName() {
        return roleName;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }

}
