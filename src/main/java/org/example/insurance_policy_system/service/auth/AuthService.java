package org.example.insurance_policy_system.service.auth;


import org.example.insurance_policy_system.dto.auth.*;
import org.example.insurance_policy_system.entity.auth.User;

public interface AuthService{

    UserView register(RegisterRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse refresh(String token);

    void logout(String token);

    void changePassword(String username, ChangePasswordRequest request);

    void forgot(ForgotPasswordRequest request);

    void reset(ResetPasswordRequest request);

    User findUser(Long id);
}

