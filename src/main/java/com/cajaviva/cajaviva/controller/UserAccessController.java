package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.UserAccessService;
import com.cajaviva.cajaviva.entity.UserAccess;
import com.cajaviva.cajaviva.exception.ForbiddenAccessException;
import com.cajaviva.cajaviva.utilities.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-accesses")
@Validated
@io.swagger.v3.oas.annotations.tags.Tag(name = "UserAccess", description = "Operaciones de acceso de usuario a cuentas")
public class UserAccessController {

    private final UserAccessService userAccessService;

    public UserAccessController(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    @GetMapping
    public List<UserAccess> getAllUserAccesses() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return userAccessService.findByUserId(currentUserId);
    }

    @GetMapping("/user/{user_id}")
    public List<UserAccess> getByUser(@PathVariable("user_id") UUID user_id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!user_id.equals(currentUserId)) {
            throw new ForbiddenAccessException("Cannot access user accesses of another user");
        }
        return userAccessService.findByUserId(currentUserId);
    }

    @GetMapping("/account/{account_id}")
    public List<UserAccess> getByAccount(@PathVariable("account_id") UUID account_id) {
        return userAccessService.findByAccountId(account_id);
    }

    @PostMapping
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Crear accesos de usuario a cuenta",
        description = "Crea un nuevo UserAccess",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserAccess.class),
            examples = { @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\n  \"accountId\": \"...\", \"role\": 1, \"userId\": \"...\"\n}") })
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Creado",
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserAccess.class)))
        }
    )
    public UserAccess createUserAccess(@RequestBody UserAccess userAccess) {
        return userAccessService.create(userAccess);
    }

    @GetMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Obtener UserAccess por ID",
        description = "Obtiene un acceso de usuario por UUID",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "UUID de UserAccess", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Acceso encontrado",
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserAccess.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No encontrado")
        }
    )
    public UserAccess getById(@PathVariable("id") UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        UserAccess userAccess = userAccessService.findById(id);
        if (!userAccess.getUserId().equals(currentUserId)) {
            throw new ForbiddenAccessException("User access does not belong to the current user");
        }
        return userAccess;
    }

    @PutMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Actualizar acceso de usuario",
        description = "Actualiza acceso por UUID",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "UUID de UserAccess")
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserAccess.class),
            examples = { @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\n  \"accountId\": \"...\", \"role\": 2, \"userId\": \"...\"\n}") })
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Actualizado",
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserAccess.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No encontrado")
        }
    )
    public UserAccess update(@PathVariable("id") UUID id, @RequestBody UserAccess userAccess) {
        return userAccessService.update(id, userAccess);
    }

    @DeleteMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Eliminar acceso de usuario",
        description = "Elimina acceso por UUID",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "UUID de UserAccess")
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Eliminado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No encontrado")
        }
    )
    public void delete(@PathVariable("id") UUID id) {
        userAccessService.delete(id);
    }
}
