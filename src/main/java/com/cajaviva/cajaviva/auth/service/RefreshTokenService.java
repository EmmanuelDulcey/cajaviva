package com.cajaviva.cajaviva.auth.service;

import com.cajaviva.cajaviva.entity.RefreshToken;
import com.cajaviva.cajaviva.repository.JPA.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken save(UUID userId, String rawToken, LocalDateTime expiresAt) {
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setTokenHash(hash(rawToken));
        token.setExpiresAt(expiresAt);
        token.setCreatedAt(LocalDateTime.now());
        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByRawToken(String rawToken) {
        return refreshTokenRepository.findByTokenHash(hash(rawToken));
    }

    public void revoke(RefreshToken token, String replacedByRawToken) {
        token.setRevokedAt(LocalDateTime.now());
        if (replacedByRawToken != null) {
            token.setReplacedByTokenHash(hash(replacedByRawToken));
        }
        refreshTokenRepository.save(token);
    }

    public void revokeAllByUserId(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public boolean isUsable(RefreshToken token) {
        return token.getRevokedAt() == null && token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
