package com.cajaviva.cajaviva.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "LiquidityProjections")
public class LiquidityProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @Column(name = "calculation_date", nullable = false)
    private LocalDateTime calculationDate;

    @NotNull
    @Column(name = "projected_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal projectedBalance;

    @NotNull
    @Column(name = "projection_date", nullable = false)
    private LocalDate projectionDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
