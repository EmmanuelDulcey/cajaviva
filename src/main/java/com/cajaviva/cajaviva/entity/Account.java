package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "account_type", nullable = false)
    private Integer accountType;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relación con User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // getters y setters
}
