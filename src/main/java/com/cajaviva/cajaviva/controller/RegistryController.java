package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dto.RegisterUserRequest;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.service.RegistryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registries")
@Tag(name = "Registry", description = "Registro publico de usuarios")
public class RegistryController {

    private final RegistryService registryService;

    public RegistryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostMapping("/users")
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea un usuario para autenticacion futura.",
            security = {}
    )
    @ApiResponse(responseCode = "201", description = "Usuario registrado", content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "400", description = "Request invalido")
    @ApiResponse(responseCode = "409", description = "Email ya existe")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserRequest request) {
        User created = registryService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
