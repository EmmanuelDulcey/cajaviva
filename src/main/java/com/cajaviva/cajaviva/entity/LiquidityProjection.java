package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "LiquidityProjections")
public class LiquidityProjection {
    @Id
    @GeneratedValue
    @Column(name = "projection_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    private LocalDateTime calculationDate;
    private double projectedBalance;
    private LocalDate projectionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // getters y setters
}
