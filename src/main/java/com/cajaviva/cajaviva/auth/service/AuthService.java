package com.cajaviva.cajaviva.auth.service;

import com.cajaviva.cajaviva.auth.config.JwtProperties;
import com.cajaviva.cajaviva.auth.dto.AuthResponse;
import com.cajaviva.cajaviva.auth.dto.LoginRequest;
import com.cajaviva.cajaviva.entity.AuthUser;
import com.cajaviva.cajaviva.entity.RefreshToken;
import com.cajaviva.cajaviva.exception.AuthenticationFailedException;
import com.cajaviva.cajaviva.exception.BusinessValidationException;
import com.cajaviva.cajaviva.repository.JPA.AuthUserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    public AuthService(
            AuthUserRepository authUserRepository,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            PasswordEncoder passwordEncoder,
            JwtProperties jwtProperties
    ) {
        this.authUserRepository = authUserRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProperties = jwtProperties;
    }

    public AuthResult login(LoginRequest request) {
        AuthUser user = authUserRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new AuthenticationFailedException("Credenciales invalidas."));
        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordDigest())) {
            throw new AuthenticationFailedException("Credenciales invalidas.");
        }
        return issueTokens(user);
    }

    public AuthResult refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthenticationFailedException("Refresh token invalido.");
        }
        Claims claims;
        try {
            claims = jwtService.parseAndValidate(refreshToken);
        } catch (Exception ex) {
            throw new AuthenticationFailedException("Refresh token invalido.");
        }
        if (jwtService.extractTokenType(claims) != JwtService.TokenType.REFRESH) {
            throw new AuthenticationFailedException("Token invalido.");
        }

        RefreshToken stored = refreshTokenService.findByRawToken(refreshToken)
                .orElseThrow(() -> new AuthenticationFailedException("Refresh token invalido."));
        if (!refreshTokenService.isUsable(stored)) {
            throw new AuthenticationFailedException("Refresh token expirado o revocado.");
        }

        UUID userId = UUID.fromString(claims.getSubject());
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessValidationException("Usuario no encontrado."));

        AuthResult result = issueTokens(user);
        refreshTokenService.revoke(stored, result.refreshToken());
        return result;
    }

    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }
        refreshTokenService.findByRawToken(refreshToken).ifPresent(rt -> refreshTokenService.revoke(rt, null));
    }

    public AuthResponse me(UUID userId) {
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessValidationException("Usuario no encontrado."));
        return toResponse(user);
    }

    private AuthResult issueTokens(AuthUser user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        LocalDateTime accessExpiresAt = LocalDateTime.now().plusMinutes(jwtProperties.getAccessTtlMinutes());
        LocalDateTime refreshExpiresAt = LocalDateTime.now().plusDays(jwtProperties.getRefreshTtlDays());
        refreshTokenService.save(user.getId(), refreshToken, refreshExpiresAt);
        return new AuthResult(accessToken, refreshToken, toResponse(user, accessExpiresAt));
    }

    private AuthResponse toResponse(AuthUser user) {
        return toResponse(user, LocalDateTime.now().plusMinutes(jwtProperties.getAccessTtlMinutes()));
    }

    private AuthResponse toResponse(AuthUser user, LocalDateTime expiresAt) {
        return new AuthResponse(user.getId(), user.getEmail(), List.of("ROLE_USER"), expiresAt);
    }

    public Duration accessTtl() {
        return Duration.ofMinutes(jwtProperties.getAccessTtlMinutes());
    }

    public Duration refreshTtl() {
        return Duration.ofDays(jwtProperties.getRefreshTtlDays());
    }

    public record AuthResult(String accessToken, String refreshToken, AuthResponse response) {
    }
}
