package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.controller.dto.CategoryUpsertRequest;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Validated
@Tag(name = "Category", description = "Operaciones CRUD sobre categorias")
public class CategoryController {
  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  @Operation(
      summary = "Listar todas las categorias",
      tags = {"Category"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de categorias",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "array", implementation = Category.class)))
      })
  public List<Category> getAllCategories() {
    return categoryService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Obtener categoria por ID",
      tags = {"Category"},
      parameters = {
        @Parameter(
            name = "id",
            description = "UUID de la categoria",
            example = "11111111-2222-3333-4444-555555555555")
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoria encontrada",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
      })
  public Category getCategoryById(@PathVariable UUID id) {
    return categoryService.findById(id);
  }

  @PostMapping
  @Operation(
      summary = "Crear categoria",
      tags = {"Category"},
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = CategoryUpsertRequest.class),
                      examples = {
                        @ExampleObject(
                            value =
                                "{\n  \"name\": \"Alimentacion\",\n  \"type\": 1,\n  \"description\": \"Gastos de comida y restaurante\"\n}")
                      })),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoria creada",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)))
      })
  public Category createCategory(@RequestBody CategoryUpsertRequest request) {
    Category category = new Category();
    category.setName(request.getName());
    category.setType(request.getType());
    category.setDescription(request.getDescription());
    return categoryService.create(category);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Actualizar categoria",
      tags = {"Category"},
      parameters = {
        @Parameter(
            name = "id",
            description = "UUID de la categoria",
            example = "11111111-2222-3333-4444-555555555555")
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = CategoryUpsertRequest.class),
                      examples = {
                        @ExampleObject(
                            value =
                                "{\n  \"name\": \"Alimentacion actualizada\",\n  \"type\": 1,\n  \"description\": \"Gastos de comida, restaurante y supermercado\"\n}")
                      })),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoria actualizada",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
      })
  public Category updateCategory(
      @PathVariable UUID id, @RequestBody CategoryUpsertRequest request) {
    Category category = new Category();
    category.setName(request.getName());
    category.setType(request.getType());
    category.setDescription(request.getDescription());
    return categoryService.update(id, category);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Eliminar categoria",
      tags = {"Category"},
      parameters = {
        @Parameter(
            name = "id",
            description = "UUID de la categoria",
            example = "11111111-2222-3333-4444-555555555555")
      },
      responses = {
        @ApiResponse(responseCode = "200", description = "Categoria eliminada"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
      })
  public void deleteCategory(@PathVariable UUID id) {
    categoryService.delete(id);
  }
}
