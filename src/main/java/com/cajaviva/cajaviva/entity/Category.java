package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    private String name;
    private int type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getters y setters
}
