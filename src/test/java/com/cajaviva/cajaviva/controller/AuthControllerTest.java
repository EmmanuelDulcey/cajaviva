package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.auth.controller.AuthController;
import com.cajaviva.cajaviva.auth.dto.AuthResponse;
import com.cajaviva.cajaviva.auth.service.JwtService;
import com.cajaviva.cajaviva.auth.service.AuthService;
import com.cajaviva.cajaviva.auth.service.CookieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private CookieService cookieService;

    @MockBean
    private JwtService jwtService;

    @Test
    void loginReturns200AndUserPayload() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthResponse response = new AuthResponse(userId, "user@test.com", List.of("ROLE_USER"), LocalDateTime.now().plusMinutes(15));
        AuthService.AuthResult result = new AuthService.AuthResult("access", "refresh", response);

        when(authService.login(any())).thenReturn(result);
        when(authService.accessTtl()).thenReturn(Duration.ofMinutes(15));
        when(authService.refreshTtl()).thenReturn(Duration.ofDays(7));
        doNothing().when(cookieService).addAccessCookie(any(), any(), any());
        doNothing().when(cookieService).addRefreshCookie(any(), any(), any());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "user@test.com",
                                  "password": "Secret123*"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }
}
