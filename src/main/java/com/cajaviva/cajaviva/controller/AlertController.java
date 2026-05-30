package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.AlertService;
import com.cajaviva.cajaviva.entity.Alert;
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
@RequestMapping("/api/alerts")
@Validated
@Tag(name = "Alert", description = "Operaciones CRUD sobre alertas")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las alertas", tags = {"Alert"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de alertas",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Alert.class)))
        }
    )
    public List<Alert> getAllAlerts() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return alertService.findByUserId(currentUserId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener alerta por ID", tags = {"Alert"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la alerta", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Alerta encontrada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Alert.class))),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
        }
    )
    public Alert getAlertById(@PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        Alert alert = alertService.findById(id);
        if (!alert.getLiquidityProjection().getAccount().getUserId().equals(currentUserId)) {
            throw new ForbiddenAccessException("Alert does not belong to the current user");
        }
        return alert;
    }

    @GetMapping("/projection/{projection_id}")
    @Operation(summary = "Listar alertas por proyección", tags = {"Alert"},
        parameters = {
            @Parameter(name = "projection_id", description = "UUID de la proyección de liquidez", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de alertas de la proyección",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Alert.class)))
        }
    )
    public List<Alert> getByProjection(@PathVariable("projection_id") UUID projection_id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        List<Alert> alerts = alertService.findByLiquidityProjectionId(projection_id);
        if (!alerts.isEmpty() && !alerts.get(0).getLiquidityProjection().getAccount().getUserId().equals(currentUserId)) {
            throw new ForbiddenAccessException("Projection does not belong to the current user");
        }
        return alerts;
    }

    @PostMapping
    @Operation(summary = "Crear alerta", tags = {"Alert"},
        requestBody = @RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Alert.class),
                examples = {@ExampleObject(value = "{\n  \"type\": 1,\n  \"message\": \"Alerta de bajo saldo\",\n  \"status\": 1,\n  \"liquidityProjection\": {\"id\": \"11111111-2222-3333-4444-555555555555\"}\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Alerta creada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Alert.class)))
        }
    )
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.create(alert);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar alerta", tags = {"Alert"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la alerta", example = "11111111-2222-3333-4444-555555555555")
        },
        requestBody = @RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Alert.class),
                examples = {@ExampleObject(value = "{\n  \"type\": 2,\n  \"message\": \"Alerta actualizada\",\n  \"status\": 2,\n  \"liquidityProjection\": {\"id\": \"11111111-2222-3333-4444-555555555555\"}\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Alerta actualizada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Alert.class))),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
        }
    )
    public Alert updateAlert(@PathVariable UUID id, @RequestBody Alert alert) {
        return alertService.update(id, alert);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar alerta", tags = {"Alert"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la alerta", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Alerta eliminada"),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
        }
    )
    public void deleteAlert(@PathVariable UUID id) {
        alertService.delete(id);
    }
}