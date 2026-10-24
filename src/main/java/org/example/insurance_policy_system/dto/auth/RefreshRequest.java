package org.example.insurance_policy_system.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RefreshRequest {
    @NotBlank private String refreshToken;

    public RefreshRequest() {
    }

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String refreshToken() {
        return refreshToken;
    }

}
