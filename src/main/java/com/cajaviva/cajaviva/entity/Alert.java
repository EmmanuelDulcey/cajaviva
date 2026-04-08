package com.cajaviva.cajaviva.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Alert {

    private UUID id;
    private Integer type;
    private String message;
    private LocalDate date;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔥 RELACIÓN MANUAL (JDBC)
    private UUID liquidityProjectionId;

    public Alert() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getLiquidityProjectionId() {
        return liquidityProjectionId;
    }

    public void setLiquidityProjectionId(UUID liquidityProjectionId) {
        this.liquidityProjectionId = liquidityProjectionId;
    }
}