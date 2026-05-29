package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.service.AccountService;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.exception.ForbiddenAccessException;
import com.cajaviva.cajaviva.utilities.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@Validated
@Tag(name = "Account", description = "Operaciones sobre cuentas bancarias")
public class AccountController {
    private final AccountService accountService;
    private final com.cajaviva.cajaviva.service.LiquidityProjectionService liqudityProjectionService;

    public AccountController(AccountService accountService, com.cajaviva.cajaviva.service.LiquidityProjectionService liqudityProjectionService) {
        this.accountService = accountService;
        this.liqudityProjectionService = liqudityProjectionService;
    }

    @GetMapping
@Operation(summary = "Obtener todas las cuentas", tags = {"Account"},
    responses = {
        @ApiResponse(responseCode = "200", description = "Lista de cuentas",
            content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Account.class)))
    }
)
public ResponseEntity<List<Account>> getAllAccounts() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        List<Account> accounts = accountService.findByUserId(currentUserId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/{user_id}")
@Operation(summary = "Listar cuentas por usuario", tags = {"Account"},
    description = "Obtiene las cuentas de un usuario por su UUID",
    parameters = {
        @Parameter(name = "user_id", description = "UUID del usuario", example = "11111111-2222-3333-4444-555555555555")
    },
    responses = {
        @ApiResponse(responseCode = "200", description = "Lista de cuentas",
            content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Account.class)))
    }
)
public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable("user_id") UUID userId) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!userId.equals(currentUserId)) {
            throw new ForbiddenAccessException("Cannot access accounts of another user");
        }
        List<Account> accounts = accountService.findByUserId(currentUserId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
@Operation(summary = "Obtener cuenta por ID", tags = {"Account"},
    parameters = {
        @Parameter(name = "id", description = "UUID de la cuenta", example = "11111111-2222-3333-4444-555555555555")
    },
    responses = {
        @ApiResponse(responseCode = "200", description = "Cuenta encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    }
)
public ResponseEntity<com.cajaviva.cajaviva.dto.AccountWithProjectionResponse> getAccountById(@PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        Account account = accountService.findById(id);
        if (account == null) throw new ResourceNotFoundException("Account not found");
        if (!account.getUserId().equals(currentUserId)) {
            throw new ForbiddenAccessException("Account does not belong to the current user");
        }
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate start = today.plusDays(1);
        java.time.LocalDate end = start.plusDays(30);
        java.util.List<com.cajaviva.cajaviva.entity.LiquidityProjection> projection = liqudityProjectionService.calculateProjection(id, start, end);
        com.cajaviva.cajaviva.dto.AccountWithProjectionResponse resp = new com.cajaviva.cajaviva.dto.AccountWithProjectionResponse();
        resp.setAccount(account);
        resp.setProjection(projection);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
@Operation(summary = "Crear cuenta", tags = {"Account"},
    requestBody = @RequestBody(
        required = true,
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class),
            examples = {@ExampleObject(value = "{\n  \"userId\": \"11111111-2222-3333-4444-555555555555\",\n  \"name\": \"Cuenta Corriente\",\n  \"accountType\": 1,\n  \"balance\": 1000.0\n}")}
        )
    ),
    responses = {
        @ApiResponse(responseCode = "201", description = "Cuenta creada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)))
    }
)
public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        account.setUserId(currentUserId);
        Account created = accountService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
@Operation(summary = "Actualizar cuenta", tags = {"Account"},
    parameters = {
        @Parameter(name = "id", description = "UUID de la cuenta", example = "11111111-2222-3333-4444-555555555555")
    },
    requestBody = @RequestBody(
        required = true,
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class),
            examples = {@ExampleObject(value = "{\n  \"userId\": \"11111111-2222-3333-4444-555555555555\",\n  \"name\": \"Cuenta Modificada\",\n  \"accountType\": 2,\n  \"balance\": 1500.0\n}")}
        )
    ),
    responses = {
        @ApiResponse(responseCode = "200", description = "Cuenta actualizada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    }
)
public ResponseEntity<Account> updateAccount(@PathVariable UUID id, @RequestBody Account account) {
        Account updated = accountService.update(id, account);
        if (updated == null) throw new ResourceNotFoundException("Account not found");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
@Operation(summary = "Eliminar cuenta", tags = {"Account"},
    parameters = {
        @Parameter(name = "id", description = "UUID de la cuenta a eliminar", example = "11111111-2222-3333-4444-555555555555")
    },
    responses = {
        @ApiResponse(responseCode = "204", description = "Cuenta eliminada (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    }
)
public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

