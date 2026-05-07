package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.dto.RegisterUserRequest;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.exception.ConflictException;
import com.cajaviva.cajaviva.repository.JPA.AuthUserRepository;
import com.cajaviva.cajaviva.service.RegistryService;
import com.cajaviva.cajaviva.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistryServiceImpl implements RegistryService {

    private final UserService userService;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistryServiceImpl(UserService userService, AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegisterUserRequest request) {
        authUserRepository.findByEmailIgnoreCase(request.email()).ifPresent(existing -> {
            throw new ConflictException("El correo ya esta registrado.");
        });

        User user = new User();
        user.setName(request.name());
        user.setLastName(request.lastName());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordDigest(passwordEncoder.encode(request.password()));
        user.setActive(true);

        return userService.create(user);
    }
}
