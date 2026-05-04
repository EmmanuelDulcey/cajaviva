package com.cajaviva.cajaviva.entity;

import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
