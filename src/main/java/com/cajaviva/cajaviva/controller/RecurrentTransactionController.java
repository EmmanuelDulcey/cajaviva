package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.RecurrentTransactionService;
import com.cajaviva.cajaviva.entity.RecurrentTransaction;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recurrent-transactions")
@Validated
@Tag(name = "RecurrentTransaction", description = "Operaciones CRUD sobre transacciones recurrentes")
public class RecurrentTransactionController {

    private final RecurrentTransactionService recurrentTransactionService;

    public RecurrentTransactionController(RecurrentTransactionService recurrentTransactionService) {
        this.recurrentTransactionService = recurrentTransactionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las transacciones recurrentes", tags = {"RecurrentTransaction"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones recurrentes",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = RecurrentTransaction.class)))
        }
    )
    public List<RecurrentTransaction> getAllRecurrentTransactions() {
        return recurrentTransactionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transacción recurrente por ID", tags = {"RecurrentTransaction"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la transacción recurrente", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción recurrente encontrada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecurrentTransaction.class))),
            @ApiResponse(responseCode = "404", description = "Transacción recurrente no encontrada")
        }
    )
    public RecurrentTransaction getRecurrentTransactionById(@PathVariable UUID id) {
        return recurrentTransactionService.findById(id);
    }

    @GetMapping("/account/{account_id}")
    @Operation(summary = "Listar transacciones recurrentes por cuenta", tags = {"RecurrentTransaction"},
        parameters = {
            @Parameter(name = "account_id", description = "UUID de la cuenta", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones recurrentes de la cuenta",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = RecurrentTransaction.class)))
        }
    )
    public List<RecurrentTransaction> getByAccount(@PathVariable("account_id") UUID account_id) {
        return recurrentTransactionService.findByAccountId(account_id);
    }

    @PostMapping
    @Operation(summary = "Crear transacción recurrente", tags = {"RecurrentTransaction"},
        requestBody = @RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecurrentTransaction.class),
                examples = {@ExampleObject(value = "{\n  \"amount\": 500.00,\n  \"description\": \"Pago de internet mensual\",\n  \"frequency\": 1,\n  \"startDate\": \"2026-01-01\",\n  \"endDate\": \"2026-12-31\",\n  \"status\": 1,\n  \"account\": {\"id\": \"11111111-2222-3333-4444-555555555555\"},\n  \"category\": {\"id\": \"22222222-3333-4444-5555-666666666666\"}\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción recurrente creada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecurrentTransaction.class)))
        }
    )
    public RecurrentTransaction createRecurrentTransaction(@RequestBody RecurrentTransaction recurrentTransaction) {
        return recurrentTransactionService.create(recurrentTransaction);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar transacción recurrente", tags = {"RecurrentTransaction"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la transacción recurrente", example = "11111111-2222-3333-4444-555555555555")
        },
        requestBody = @RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecurrentTransaction.class),
                examples = {@ExampleObject(value = "{\n  \"amount\": 600.00,\n  \"description\": \"Pago de internet actualizado\",\n  \"frequency\": 1,\n  \"startDate\": \"2026-01-01\",\n  \"endDate\": \"2027-12-31\",\n  \"status\": 2,\n  \"account\": {\"id\": \"11111111-2222-3333-4444-555555555555\"},\n  \"category\": {\"id\": \"22222222-3333-4444-5555-666666666666\"}\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción recurrente actualizada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecurrentTransaction.class))),
            @ApiResponse(responseCode = "404", description = "Transacción recurrente no encontrada")
        }
    )
    public RecurrentTransaction updateRecurrentTransaction(@PathVariable UUID id, @RequestBody RecurrentTransaction recurrentTransaction) {
        return recurrentTransactionService.update(id, recurrentTransaction);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar transacción recurrente", tags = {"RecurrentTransaction"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la transacción recurrente", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción recurrente eliminada"),
            @ApiResponse(responseCode = "404", description = "Transacción recurrente no encontrada")
        }
    )
    public void deleteRecurrentTransaction(@PathVariable UUID id) {
        recurrentTransactionService.delete(id);
    }
}