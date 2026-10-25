package org.example.insurance_policy_system.serviceimpl.auth;


import org.example.authservice.config.JwtService;


import org.example.insurance_policy_system.dto.auth.*;
import org.example.insurance_policy_system.entity.auth.PasswordResetToken;
import org.example.insurance_policy_system.entity.auth.RefreshToken;
import org.example.insurance_policy_system.entity.auth.Role;
import org.example.insurance_policy_system.entity.auth.User;
import org.example.insurance_policy_system.enums.auth.UserStatus;
import org.example.insurance_policy_system.repository.auth.PasswordResetTokenRepository;
import org.example.insurance_policy_system.repository.auth.RefreshTokenRepository;
import org.example.insurance_policy_system.repository.auth.RoleRepository;
import org.example.insurance_policy_system.repository.auth.UserRepository;
import org.example.insurance_policy_system.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordResetTokenRepository passwordResetTokens;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.password-reset.token-expiration-minutes:15}")
    private long resetTokenExpirationMinutes;

    @Value("${app.password-reset.delivery-mode:console}")
    private String resetTokenDeliveryMode;

    public AuthServiceImpl(
            UserRepository users,
            RoleRepository roles,
            RefreshTokenRepository refreshTokens,
            PasswordResetTokenRepository passwordResetTokens,
            PasswordEncoder encoder,
            JwtService jwt) {
        this.users = users;
        this.roles = roles;
        this.refreshTokens = refreshTokens;
        this.passwordResetTokens = passwordResetTokens;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    private User activeUser(String login) {
        User user = users.findByUsername(login)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid credentials"
                ));

        if (user.getStatus() != UserStatus.Active) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not active"
            );
        }

        return user;
    }

    private UserView view(User user) {
        return new UserView(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMobile(),
                user.getRole().getRoleName(),
                user.getStatus().name()
        );
    }

    private TokenResponse issue(User user) {
        String accessToken = jwt.generateAccessToken(
                user.getUsername(),
                user.getRole().getRoleName()
        );

        String refreshToken =
                UUID.randomUUID() + "." + UUID.randomUUID();

        RefreshToken refresh = new RefreshToken();
        refresh.setToken(refreshToken);
        refresh.setUser(user);
        refresh.setExpiresAt(
                Instant.now().plusMillis(604800000)
        );

        refreshTokens.save(refresh);

        return new TokenResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwt.accessSeconds(),
                view(user)
        );
    }

    @Override
    @Transactional
    public UserView register(RegisterRequest request) {
        if (users.existsByUsername(request.username())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }

        if (request.email() != null
                && users.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        if (request.mobile() != null
                && users.existsByMobile(request.mobile())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Mobile already exists"
            );
        }

        Role role = roles.findByRoleNameIgnoreCase(request.roleName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Role does not exist; create/configure role first"
                ));

        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(encoder.encode(request.password()));
        user.setEmail(request.email());
        user.setMobile(request.mobile());
        user.setRole(role);
        user.setStatus(UserStatus.Active);

        return view(users.save(user));
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = users.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid credentials"
                ));

        if (!encoder.matches(
                request.password(),
                user.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }

        if (user.getStatus() != UserStatus.Active) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not active"
            );
        }

        return issue(user);
    }

    @Override
    @Transactional
    public TokenResponse refresh(String token) {
        RefreshToken oldToken = refreshTokens.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid refresh token"
                ));

        if (oldToken.isRevoked()
                || oldToken.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Refresh token expired or revoked"
            );
        }

        oldToken.setRevoked(true);
        refreshTokens.save(oldToken);

        return issue(oldToken.getUser());
    }

    @Override
    @Transactional
    public void logout(String token) {
        RefreshToken refreshToken = refreshTokens.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Refresh token not found"
                ));

        refreshToken.setRevoked(true);
        refreshTokens.save(refreshToken);
    }

    @Override
    @Transactional
    public void changePassword(
            String username,
            ChangePasswordRequest request) {
        User user = activeUser(username);

        if (!encoder.matches(
                request.currentPassword(),
                user.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Current password is incorrect"
            );
        }

        user.setPasswordHash(
                encoder.encode(request.newPassword())
        );
        users.save(user);

        refreshTokens.findAllByUserIdAndRevokedFalse(user.getId())
                .forEach(token -> {
                    token.setRevoked(true);
                    refreshTokens.save(token);
                });
    }

    @Override
    @Transactional
    public void forgot(ForgotPasswordRequest request) {
        String identifier = request.getUsernameOrEmail() == null
                ? ""
                : request.getUsernameOrEmail().trim();

        Optional<User> userOptional =
                users.findByUsernameIgnoreCaseOrEmailIgnoreCase(
                        identifier,
                        identifier
                );

        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();

        if (user.getStatus() != UserStatus.Active) {
            return;
        }

        passwordResetTokens.findAllByUserIdAndUsedFalse(user.getId())
                .forEach(token -> {
                    token.setUsed(true);
                    passwordResetTokens.save(token);
                });

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setTokenHash(sha256(rawToken));
        resetToken.setUser(user);
        resetToken.setExpiresAt(
                Instant.now().plusSeconds(
                        resetTokenExpirationMinutes * 60
                )
        );
        resetToken.setUsed(false);

        passwordResetTokens.save(resetToken);

        if ("console".equalsIgnoreCase(resetTokenDeliveryMode)) {
            System.out.println(
                    "\n========== LOCAL PASSWORD RESET TOKEN =========="
            );
            System.out.println("Account: " + user.getUsername());
            System.out.println("Reset token: " + rawToken);
            System.out.println(
                    "Expires at: " + resetToken.getExpiresAt()
            );
            System.out.println(
                    "===============================================\n"
            );
        } else {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Password reset delivery is not configured. "
                            + "Use console mode only for local testing."
            );
        }
    }

    @Override
    @Transactional
    public void reset(ResetPasswordRequest request) {
        String identifier = request.getUsernameOrEmail() == null
                ? ""
                : request.getUsernameOrEmail().trim();

        User user = users.findByUsernameIgnoreCaseOrEmailIgnoreCase(
                        identifier,
                        identifier
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid or expired reset token"
                ));

        PasswordResetToken resetToken =
                passwordResetTokens.findByTokenHash(
                        sha256(request.getResetToken())
                ).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid or expired reset token"
                ));

        if (!resetToken.getUser().getId().equals(user.getId())
                || resetToken.isUsed()
                || !resetToken.getExpiresAt().isAfter(Instant.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid or expired reset token"
            );
        }

        if (user.getStatus() != UserStatus.Active) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not active"
            );
        }

        user.setPasswordHash(
                encoder.encode(request.getNewPassword())
        );
        users.save(user);

        resetToken.setUsed(true);
        passwordResetTokens.save(resetToken);

        refreshTokens.findAllByUserIdAndRevokedFalse(user.getId())
                .forEach(token -> {
                    token.setRevoked(true);
                    refreshTokens.save(token);
                });
    }

    private String sha256(String value) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    value.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder hex = new StringBuilder(hash.length * 2);

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 is not available",
                    exception
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }
}

