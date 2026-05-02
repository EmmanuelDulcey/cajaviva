package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts") // nombre de la tabla en tu BD
public class Alert {

    @Id
    @GeneratedValue
    @Column(name = "alert_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "message", length = 500)
    private String message;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 🔥 Relación JPA con LiquidityProjection
    @ManyToOne
    @JoinColumn(name = "liquidity_projection_id", nullable = false)
    private LiquidityProjection liquidityProjection;

    public Alert() {}

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LiquidityProjection getLiquidityProjection() { return liquidityProjection; }
    public void setLiquidityProjection(LiquidityProjection liquidityProjection) { this.liquidityProjection = liquidityProjection; }

    public Object getLiquidityProjectionId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLiquidityProjectionId'");
    }
}
