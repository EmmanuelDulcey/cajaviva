package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.LiquidityProjectionService;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.dto.LiquidityProjectionRequest;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/liquidity-projections")
@Validated
@Tag(name = "LiquidityProjection", description = "Operaciones CRUD sobre proyecciones de liquidez")
public class LiquidityProjectionController {

    private final LiquidityProjectionService liquidityProjectionService;

    public LiquidityProjectionController(LiquidityProjectionService liquidityProjectionService) {
        this.liquidityProjectionService = liquidityProjectionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las proyecciones", tags = {"LiquidityProjection"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de proyecciones",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = LiquidityProjection.class)))
        }
    )
    public List<LiquidityProjection> getAllProjections() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return liquidityProjectionService.findByUserId(currentUserId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener proyección por ID", tags = {"LiquidityProjection"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la proyección", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Proyección encontrada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LiquidityProjection.class))),
            @ApiResponse(responseCode = "404", description = "Proyección no encontrada")
        }
    )
    public LiquidityProjection getProjectionById(@PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        LiquidityProjection projection = liquidityProjectionService.findById(id);
        if (!projection.getAccount().getUserId().equals(currentUserId)) {
            throw new ForbiddenAccessException("Projection does not belong to the current user");
        }
        return projection;
    }

    @GetMapping("/account/{account_id}")
    @Operation(summary = "Listar proyecciones por cuenta", tags = {"LiquidityProjection"},
        parameters = {
            @Parameter(name = "account_id", description = "UUID de la cuenta", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de proyecciones de la cuenta",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = LiquidityProjection.class)))
        }
    )
    public List<LiquidityProjection> getByAccount(@PathVariable("account_id") UUID account_id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        List<LiquidityProjection> projections = liquidityProjectionService.findByAccountId(account_id);
        if (!projections.isEmpty() && !projections.get(0).getAccount().getUserId().equals(currentUserId)) {
            throw new ForbiddenAccessException("Account does not belong to the current user");
        }
        return projections;
    }

    @PostMapping
    @Operation(summary = "Crear proyección", tags = {"LiquidityProjection"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LiquidityProjection.class),
                examples = {@ExampleObject(value = "{\n  \"projectedBalance\": 5000.00,\n  \"projectionDate\": \"2026-06-01\",\n  \"notes\": \"Proyección mensual\",\n  \"account\": {\"id\": \"11111111-2222-3333-4444-555555555555\"}\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Proyección creada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LiquidityProjection.class)))
        }
    )
    public LiquidityProjection createProjection(@RequestBody LiquidityProjection projection) {
        return liquidityProjectionService.create(projection);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proyección", tags = {"LiquidityProjection"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la proyección", example = "11111111-2222-3333-4444-555555555555")
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LiquidityProjection.class),
                examples = {@ExampleObject(value = "{\n  \"projectedBalance\": 7500.00,\n  \"projectionDate\": \"2026-06-15\",\n  \"notes\": \"Proyección actualizada\",\n  \"account\": {\"id\": \"11111111-2222-3333-4444-555555555555\"}\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Proyección actualizada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LiquidityProjection.class))),
            @ApiResponse(responseCode = "404", description = "Proyección no encontrada")
        }
    )
    public LiquidityProjection updateProjection(@PathVariable UUID id, @RequestBody LiquidityProjection projection) {
        return liquidityProjectionService.update(id, projection);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proyección", tags = {"LiquidityProjection"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la proyección", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Proyección eliminada"),
            @ApiResponse(responseCode = "404", description = "Proyección no encontrada")
        }
    )
    public void deleteProjection(@PathVariable UUID id) {
        liquidityProjectionService.delete(id);
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calcular proyección de liquidez en rango de fechas", tags = {"LiquidityProjection"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LiquidityProjectionRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de proyecciones calculadas",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = LiquidityProjection.class)))
        }
    )
    public List<LiquidityProjection> calculateProjection(@RequestBody LiquidityProjectionRequest req) {
        return liquidityProjectionService.calculateProjection(req.getAccountId(), req.getStartDate(), req.getEndDate());
    }
}
