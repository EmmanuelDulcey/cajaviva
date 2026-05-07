package com.cajaviva.cajaviva.repository.JPA;

import com.cajaviva.cajaviva.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    void deleteByUserId(UUID userId);
    long deleteByRevokedAtIsNotNullOrExpiresAtBefore(LocalDateTime now);
}
