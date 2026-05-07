package com.cajaviva.cajaviva.auth.controller;

import com.cajaviva.cajaviva.auth.dto.AuthResponse;
import com.cajaviva.cajaviva.auth.dto.LoginRequest;
import com.cajaviva.cajaviva.auth.security.UserPrincipal;
import com.cajaviva.cajaviva.auth.service.AuthService;
import com.cajaviva.cajaviva.auth.service.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Flujo de autenticación por cookies: login emite CV_ACCESS_TOKEN y CV_REFRESH_TOKEN. Usa /auth/refresh para renovar sesión y /auth/logout para invalidarla.")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    public AuthController(AuthService authService, CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica usuario y retorna cookies seguras HttpOnly con access y refresh token.", security = {})
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthService.AuthResult result = authService.login(request);
        cookieService.addAccessCookie(response, result.accessToken(), authService.accessTtl());
        cookieService.addRefreshCookie(response, result.refreshToken(), authService.refreshTtl());
        return ResponseEntity.ok(result.response());
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh", description = "Rota refresh token y emite nueva sesión.", security = {})
    @ApiResponse(responseCode = "200", description = "Refresh exitoso", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401", description = "Refresh inválido")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = readCookie(request, CookieService.REFRESH_COOKIE);
        AuthService.AuthResult result = authService.refresh(refreshToken);
        cookieService.addAccessCookie(response, result.accessToken(), authService.accessTtl());
        cookieService.addRefreshCookie(response, result.refreshToken(), authService.refreshTtl());
        return ResponseEntity.ok(result.response());
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revoca sesión y limpia cookies de autenticación.", security = {})
    @ApiResponse(responseCode = "200", description = "Logout exitoso")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = readCookie(request, CookieService.REFRESH_COOKIE);
        authService.logout(refreshToken);
        cookieService.clearAuthCookies(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(
            summary = "Sesión actual",
            description = "Retorna la identidad autenticada actual.",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @ApiResponse(responseCode = "200", description = "Usuario autenticado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    public ResponseEntity<AuthResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(authService.me(principal.getUserId()));
    }

    private String readCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
