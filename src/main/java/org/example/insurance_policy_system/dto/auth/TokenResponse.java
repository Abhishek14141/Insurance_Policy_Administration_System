package org.example.insurance_policy_system.dto.auth;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long accessExpiresInSeconds;
    private UserView user;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken, String refreshToken, String tokenType, long accessExpiresInSeconds, UserView user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.accessExpiresInSeconds = accessExpiresInSeconds;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String accessToken() {
        return accessToken;
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

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String tokenType() {
        return tokenType;
    }

    public long getAccessExpiresInSeconds() {
        return accessExpiresInSeconds;
    }

    public void setAccessExpiresInSeconds(long accessExpiresInSeconds) {
        this.accessExpiresInSeconds = accessExpiresInSeconds;
    }

    public long accessExpiresInSeconds() {
        return accessExpiresInSeconds;
    }

    public UserView getUser() {
        return user;
    }

    public void setUser(UserView user) {
        this.user = user;
    }

    public UserView user() {
        return user;
    }

}
