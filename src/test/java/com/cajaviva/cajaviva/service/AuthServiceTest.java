package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.auth.config.JwtProperties;
import com.cajaviva.cajaviva.auth.dto.LoginRequest;
import com.cajaviva.cajaviva.auth.service.AuthService;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.auth.service.RefreshTokenService;
import com.cajaviva.cajaviva.entity.AuthUser;
import com.cajaviva.cajaviva.entity.RefreshToken;
import com.cajaviva.cajaviva.exception.AuthenticationFailedException;
import com.cajaviva.cajaviva.repository.JPA.AuthUserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthUserRepository authUserRepository;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authUserRepository = mock(AuthUserRepository.class);
        jwtService = mock(JwtService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        passwordEncoder = mock(PasswordEncoder.class);

        JwtProperties props = new JwtProperties();
        props.setAccessTtlMinutes(15);
        props.setRefreshTtlDays(7);
        props.setIssuer("cajaviva");
        props.setAudience("cajaviva-api");
        props.setSigningKey("01234567890123456789012345678901");

        authService = new AuthService(authUserRepository, jwtService, refreshTokenService, passwordEncoder, props);
    }

    @Test
    void loginSuccessReturnsTokensAndResponse() {
        AuthUser user = activeUser();
        when(authUserRepository.findByEmailIgnoreCase("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass12345", user.getPasswordDigest())).thenReturn(true);
        when(jwtService.generateAccessToken(user.getId(), user.getEmail())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user.getId())).thenReturn("refresh-token");

        AuthService.AuthResult result = authService.login(new LoginRequest("user@test.com", "pass12345"));

        assertNotNull(result);
        assertEquals("access-token", result.accessToken());
        assertEquals("refresh-token", result.refreshToken());
        assertEquals(user.getId(), result.response().userId());
        verify(refreshTokenService, times(1)).save(eq(user.getId()), eq("refresh-token"), any(LocalDateTime.class));
    }

    @Test
    void loginWithBadPasswordThrowsAuthenticationFailed() {
        AuthUser user = activeUser();
        when(authUserRepository.findByEmailIgnoreCase("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", user.getPasswordDigest())).thenReturn(false);

        assertThrows(AuthenticationFailedException.class, () -> authService.login(new LoginRequest("user@test.com", "wrong")));
    }

    @Test
    void refreshSuccessRotatesToken() {
        UUID userId = UUID.randomUUID();
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(userId.toString());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevokedAt(null);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        AuthUser user = activeUser();
        user.setId(userId);

        when(jwtService.parseAndValidate("refresh-old")).thenReturn(claims);
        when(jwtService.extractTokenType(claims)).thenReturn(JwtService.TokenType.REFRESH);
        when(refreshTokenService.findByRawToken("refresh-old")).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.isUsable(refreshToken)).thenReturn(true);
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(userId, user.getEmail())).thenReturn("access-new");
        when(jwtService.generateRefreshToken(userId)).thenReturn("refresh-new");

        AuthService.AuthResult result = authService.refresh("refresh-old");

        assertEquals("access-new", result.accessToken());
        assertEquals("refresh-new", result.refreshToken());
        verify(refreshTokenService, times(1)).revoke(refreshToken, "refresh-new");
    }

    @Test
    void refreshWithoutTokenThrowsAuthenticationFailed() {
        assertThrows(AuthenticationFailedException.class, () -> authService.refresh(null));
    }

    private AuthUser activeUser() {
        AuthUser user = new AuthUser();
        user.setId(UUID.randomUUID());
        user.setEmail("user@test.com");
        user.setPasswordDigest("$2a$10$abc");
        user.setActive(true);
        return user;
    }
}
