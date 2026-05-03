package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue
    @Column(name = "id")
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

    @ManyToOne
    @JoinColumn(name = "liquidity_projection_id", nullable = false)
    private LiquidityProjection liquidityProjection;

    public Alert() {}

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

    @Transient
    public UUID getLiquidityProjectionId() {
        return liquidityProjection != null ? liquidityProjection.getId() : null;
    }

    public void setLiquidityProjectionId(UUID liquidityProjectionId) {
        if (liquidityProjectionId == null) {
            this.liquidityProjection = null;
        } else {
            LiquidityProjection projection = new LiquidityProjection();
            projection.setId(liquidityProjectionId);
            this.liquidityProjection = projection;
        }
    }
}
