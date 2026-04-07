package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "Persons")
public class Person {
    @Id
    @GeneratedValue
    @Column(name = "person_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    private String name;
    private String lastName;
    private String email;
    private boolean active;
    private String passwordDigest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getters y setters
}
