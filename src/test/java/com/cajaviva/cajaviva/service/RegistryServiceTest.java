package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.dto.RegisterUserRequest;
import com.cajaviva.cajaviva.entity.AuthUser;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.exception.ConflictException;
import com.cajaviva.cajaviva.repository.JPA.AuthUserRepository;
import com.cajaviva.cajaviva.service.impl.RegistryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistryServiceTest {

    private UserService userService;
    private AuthUserRepository authUserRepository;
    private PasswordEncoder passwordEncoder;
    private RegistryServiceImpl registryService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        authUserRepository = mock(AuthUserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        registryService = new RegistryServiceImpl(userService, authUserRepository, passwordEncoder);
    }

    @Test
    void registerUserCreatesUserWithEncodedPassword() {
        RegisterUserRequest request = new RegisterUserRequest("Ana", "Lopez", "Ana@Test.com", "Secret123*");
        when(authUserRepository.findByEmailIgnoreCase("Ana@Test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Secret123*")).thenReturn("encoded-password");
        when(userService.create(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        User result = registryService.registerUser(request);

        assertNotNull(result.getId());
        assertEquals("ana@test.com", result.getEmail());
        assertTrue(result.isActive());
        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void registerUserWithDuplicatedEmailThrowsConflict() {
        RegisterUserRequest request = new RegisterUserRequest("Ana", "Lopez", "ana@test.com", "Secret123*");
        AuthUser existing = new AuthUser();
        when(authUserRepository.findByEmailIgnoreCase("ana@test.com")).thenReturn(Optional.of(existing));

        assertThrows(ConflictException.class, () -> registryService.registerUser(request));
        verify(userService, never()).create(any(User.class));
    }
}
