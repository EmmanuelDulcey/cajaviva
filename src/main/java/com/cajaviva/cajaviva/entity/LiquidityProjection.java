package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "liquidity_projections")
public class LiquidityProjection {

    @Id
    @GeneratedValue
    @Column(name = "projection_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "calculation_date", nullable = false)
    private LocalDateTime calculationDate;

    @Column(name = "projected_balance", nullable = false)
    private BigDecimal projectedBalance;

    @Column(name = "projection_date", nullable = false)
    private LocalDate projectionDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 🔥 Relación con Account
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public LiquidityProjection() {}

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDateTime getCalculationDate() { return calculationDate; }
    public void setCalculationDate(LocalDateTime calculationDate) { this.calculationDate = calculationDate; }

    public BigDecimal getProjectedBalance() { return projectedBalance; }
    public void setProjectedBalance(BigDecimal projectedBalance) { this.projectedBalance = projectedBalance; }

    public LocalDate getProjectionDate() { return projectionDate; }
    public void setProjectionDate(LocalDate projectionDate) { this.projectionDate = projectionDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Object getAmount() {
        throw new UnsupportedOperationException("Unimplemented method 'getAmount'");
    }

    public void setAmount(Object amount) {
        throw new UnsupportedOperationException("Unimplemented method 'setAmount'");
    }
}
