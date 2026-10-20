package org.example.insurance_policy_system.repository.auth;

import java.util.Optional;

import org.example.insurance_policy_system.entity.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    void deleteByUser_UserId(Long userId);
}