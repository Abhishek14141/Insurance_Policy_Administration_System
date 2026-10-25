package org.example.insurance_policy_system.repository.auth;
import org.example.insurance_policy_system.entity.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByUserIdAndRevokedFalse(Long userId);
}
