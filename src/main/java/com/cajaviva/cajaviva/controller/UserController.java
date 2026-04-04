package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "Gestión de usuarios")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Crear usuario")
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}