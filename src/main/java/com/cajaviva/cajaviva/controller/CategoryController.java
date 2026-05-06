package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.CategoryService;
import com.cajaviva.cajaviva.entity.Category;

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
@RequestMapping("/api/categories")
@Validated
@Tag(name = "Category", description = "Operaciones CRUD sobre categorías")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías", tags = {"Category"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Category.class)))
        }
    )
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", tags = {"Category"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la categoría", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
        }
    )
    public Category getCategoryById(@PathVariable UUID id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear categoría", tags = {"Category"},
        requestBody = @RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class),
                examples = {@ExampleObject(value = "{\n  \"name\": \"Alimentación\",\n  \"type\": 1,\n  \"description\": \"Gastos de comida y restaurante\"\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Categoría creada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
        }
    )
    public Category createCategory(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", tags = {"Category"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la categoría", example = "11111111-2222-3333-4444-555555555555")
        },
        requestBody = @RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class),
                examples = {@ExampleObject(value = "{\n  \"name\": \"Alimentación actualizada\",\n  \"type\": 1,\n  \"description\": \"Gastos de comida, restaurante y supermercado\"\n}")}
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
        }
    )
    public Category updateCategory(@PathVariable UUID id, @RequestBody Category category) {
        return categoryService.update(id, category);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", tags = {"Category"},
        parameters = {
            @Parameter(name = "id", description = "UUID de la categoría", example = "11111111-2222-3333-4444-555555555555")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
        }
    )
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
    }
}