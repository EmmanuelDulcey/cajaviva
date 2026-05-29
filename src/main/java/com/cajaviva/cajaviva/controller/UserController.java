package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.service.UserService;
import com.cajaviva.cajaviva.exception.ForbiddenAccessException;
import com.cajaviva.cajaviva.utilities.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Validated
@Tag(name = "User", description = "Operaciones CRUD sobre usuarios")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
@Operation(summary = "Listar todos los usuarios", tags = {"User"},
    responses = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios",
            content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = User.class)))
    }
)
public User getAll() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return service.findById(currentUserId);
    }

    @GetMapping("/{id}")
@Operation(summary = "Obtener usuario por ID", tags = {"User"},
    parameters = {
        @Parameter(name = "id", description = "UUID del usuario", example = "11111111-2222-3333-4444-555555555555")
    },
    responses = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    }
)
public User getById(@PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!id.equals(currentUserId)) {
            throw new ForbiddenAccessException("Cannot access another user's profile");
        }
        return service.findById(currentUserId);
    }

    @PostMapping
@Operation(summary = "Crear usuario", tags = {"User"},
    requestBody = @RequestBody(
        required = true,
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class),
            examples = {@ExampleObject(value = "{\n  \"name\": \"Juan Pérez\",\n  \"email\": \"juan.perez@ejemplo.com\"\n}")}
        )
    ),
    responses = {
        @ApiResponse(responseCode = "200", description = "Usuario creado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    }
)
public User create(@RequestBody User user) {
        return service.create(user);
    }

    @PutMapping("/{id}")
@Operation(summary = "Actualizar usuario", tags = {"User"},
    parameters = {
        @Parameter(name = "id", description = "UUID del usuario", example = "11111111-2222-3333-4444-555555555555")
    },
    requestBody = @RequestBody(
        required = true,
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class),
            examples = {@ExampleObject(value = "{\n  \"name\": \"Juan Pérez\",\n  \"email\": \"juan.nuevo@ejemplo.com\"\n}")}
        )
    ),
    responses = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    }
)
public User update(@PathVariable UUID id, @RequestBody User user) {
        return service.update(id, user);
    }

    @DeleteMapping("/{id}")
@Operation(summary = "Eliminar usuario", tags = {"User"},
    parameters = {
        @Parameter(name = "id", description = "UUID del usuario", example = "11111111-2222-3333-4444-555555555555")
    },
    responses = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    }
)
public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}