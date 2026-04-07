package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "account_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    private String name;
    private int accountType;
    private double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relación: cada Account pertenece a un Person
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // getters y setters
}
