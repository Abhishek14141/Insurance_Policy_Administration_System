package org.example.insurance_policy_system.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RegisterRequest {
    @NotBlank @Size(max=100) private String username;
    @NotBlank @Size(min=8, max=72) private String password;
    @Email @Size(max=150) private String email;
    @Size(max=20) private String mobile;
    @NotBlank private String roleName;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String email, String mobile, String roleName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String password() {
        return password;
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


}
