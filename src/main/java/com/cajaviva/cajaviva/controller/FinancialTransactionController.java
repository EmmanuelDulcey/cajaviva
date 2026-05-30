package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.FinancialTransactionService;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@Validated
@Tag(name = "FinancialTransaction", description = "Operaciones CRUD sobre transacciones financieras")
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;

    public FinancialTransactionController(FinancialTransactionService financialTransactionService) {
        this.financialTransactionService = financialTransactionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las transacciones", tags = {"FinancialTransaction"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = FinancialTransaction.class)))
        }
    )
    public List<FinancialTransaction> getAllTransactions() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return financialTransactionService.findByUserId(currentUserId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transacción por ID", tags = {"FinancialTransaction"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la transacción", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción encontrada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FinancialTransaction.class))),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
        }
    )
    public FinancialTransaction getTransactionById(@PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        FinancialTransaction transaction = financialTransactionService.findById(id);
        if (!transaction.getAccount().getUserId().equals(currentUserId)) {
            throw new ForbiddenAccessException("Transaction does not belong to the current user");
        }
        return transaction;
    }

    @GetMapping("/account/{account_id}")
    @Operation(summary = "Listar transacciones por cuenta", tags = {"FinancialTransaction"},
        parameters = {
            @Parameter(name = "account_id", description = "UUID de la cuenta", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones de la cuenta",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = FinancialTransaction.class)))
        }
    )
    public List<FinancialTransaction> getTransactionsByAccount(@PathVariable("account_id") UUID account_id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        List<FinancialTransaction> transactions = financialTransactionService.findByAccountId(account_id);
        if (!transactions.isEmpty() && !transactions.get(0).getAccount().getUserId().equals(currentUserId)) {
            throw new ForbiddenAccessException("Account does not belong to the current user");
        }
        return transactions;
    }

    @PostMapping
    @Operation(summary = "Crear transacción", tags = {"FinancialTransaction"},
        requestBody = @RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FinancialTransaction.class),
                examples = {@ExampleObject(value = "{\n  \"value\": 1000.00,\n  \"description\": \"Pago de servicios\",\n  \"date\": \"2026-05-01T10:30:00\",\n  \"status\": 1,\n  \"account\": {\"id\": \"11111111-2222-3333-4444-555555555555\"},\n  \"category\": {\"id\": \"22222222-3333-4444-5555-666666666666\"}\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción creada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FinancialTransaction.class)))
        }
    )
    public FinancialTransaction createTransaction(@RequestBody FinancialTransaction transaction) {
        return financialTransactionService.create(transaction);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar transacción", tags = {"FinancialTransaction"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la transacción", example = "11111111-2222-3333-4444-555555555555")
        },
        requestBody = @RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FinancialTransaction.class),
                examples = {@ExampleObject(value = "{\n  \"value\": 1500.00,\n  \"description\": \"Pago de servicios actualizado\",\n  \"date\": \"2026-05-02T14:00:00\",\n  \"status\": 2,\n  \"account\": {\"id\": \"11111111-2222-3333-4444-555555555555\"},\n  \"category\": {\"id\": \"22222222-3333-4444-5555-666666666666\"}\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción actualizada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FinancialTransaction.class))),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
        }
    )
    public FinancialTransaction updateTransaction(@PathVariable UUID id, @RequestBody FinancialTransaction transaction) {
        return financialTransactionService.update(id, transaction);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar transacción", tags = {"FinancialTransaction"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la transacción", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción eliminada"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
        }
    )
    public void deleteTransaction(@PathVariable UUID id) {
        financialTransactionService.delete(id);
    }
}