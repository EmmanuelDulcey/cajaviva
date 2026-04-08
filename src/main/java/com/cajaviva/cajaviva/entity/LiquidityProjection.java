package com.cajaviva.cajaviva.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class LiquidityProjection {

    private UUID id;
    private LocalDateTime calculationDate;
    private BigDecimal projectedBalance;
    private LocalDate projectionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔥 RELACIÓN JDBC
    private UUID accountId;

    public LiquidityProjection() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(LocalDateTime calculationDate) {
        this.calculationDate = calculationDate;
    }

    public BigDecimal getProjectedBalance() {
        return projectedBalance;
    }

    public void setProjectedBalance(BigDecimal projectedBalance) {
        this.projectedBalance = projectedBalance;
    }

    public LocalDate getProjectionDate() {
        return projectionDate;
    }

    public void setProjectionDate(LocalDate projectionDate) {
        this.projectionDate = projectionDate;
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

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
}