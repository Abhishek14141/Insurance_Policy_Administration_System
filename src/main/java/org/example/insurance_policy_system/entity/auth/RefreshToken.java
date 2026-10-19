package org.example.insurance_policy_system.entity.auth;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long refreshTokenId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Store a cryptographic hash of the refresh token, not the raw token.
    @Column(name = "token_hash", nullable = false, unique = true,
            length = 255)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @PrePersist
    public void beforeInsert() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Transient
    public boolean isRevoked() {
        return revokedAt != null;
    }

    @Transient
    public boolean isExpired() {
        return expiresAt == null || !expiresAt.isAfter(LocalDateTime.now());
    }
}
