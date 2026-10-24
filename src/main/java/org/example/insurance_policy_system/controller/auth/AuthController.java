package org.example.insurance_policy_system.controller.auth;
import org.example.authservice.util.ApiResponse;
import org.example.insurance_policy_system.dto.auth.*;
import org.example.insurance_policy_system.service.auth.AuthService;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
@RestController @RequestMapping("/api/v1/auth") @Validated
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService s){
        service=s;
    }
    @PostMapping("/register") public ApiResponse<UserView> register(@Valid @RequestBody RegisterRequest q){
        return ApiResponse.ok("User registered successfully",service.register(q));
    }
    @PostMapping("/login") public ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest q){
        return ApiResponse.ok("Login successful",service.login(q));
    }
    @PostMapping("/refresh-token") public ApiResponse<TokenResponse> refresh(@Valid @RequestBody RefreshRequest q){
        return ApiResponse.ok("Token refreshed",service.refresh(q.refreshToken()));
    }
    @PostMapping("/logout") public ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest q){
        service.logout(q.refreshToken());
        return ApiResponse.ok("Logout successful",null);
    }
    @PostMapping("/change-password") public ApiResponse<Void> change(@Valid @RequestBody ChangePasswordRequest q,Authentication auth){
        if(auth==null)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED,"Bearer authentication required");
        service.changePassword(auth.getName(),q);
        return ApiResponse.ok("Password changed",null);
    }
    @PostMapping("/forgot-password") public ApiResponse<Void> forgot(@Valid @RequestBody ForgotPasswordRequest q){
        service.forgot(q);
        return ApiResponse.ok("If the account exists, reset instructions will be sent",null);
    }
    @PostMapping("/reset-password") public ApiResponse<Void> reset(@Valid @RequestBody ResetPasswordRequest q){
        service.reset(q);
        return ApiResponse.ok("Password reset",null);
    }
}
