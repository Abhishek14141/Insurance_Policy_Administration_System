package org.example.insurance_policy_system.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ResetPasswordRequest {
    @NotBlank private String usernameOrEmail;
    @NotBlank private String resetToken;
    @NotBlank @Size(min=8, max=72) private String newPassword;

    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String usernameOrEmail, String resetToken, String newPassword) {
        this.usernameOrEmail = usernameOrEmail;
        this.resetToken = resetToken;
        this.newPassword = newPassword;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String usernameOrEmail() {
        return usernameOrEmail;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String resetToken() {
        return resetToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String newPassword() {
        return newPassword;
    }

}
