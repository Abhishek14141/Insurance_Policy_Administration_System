package org.example.insurance_policy_system.dto.auth;

import lombok.Data;

@Data
public class UserView {
    private Long id;
    private String username;
    private String email;
    private String mobile;
    private String role;
    private String status;

    public UserView() {
    }

    public UserView(Long id, String username, String email, String mobile, String role, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String role() {
        return role;
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
