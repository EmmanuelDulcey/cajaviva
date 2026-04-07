package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Alerts")
public class Alert {
    @Id
    @GeneratedValue
    @Column(name = "alert_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    private int type;
    private String message;
    private LocalDate date;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "liquidity_projection_id", nullable = false)
    private LiquidityProjection liquidityProjection;

    // getters y setters
}
