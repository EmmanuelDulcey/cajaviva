package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "FinancialTransactions")
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- GETTERS Y SETTERS ---
    public UUID getTransactionId() { return transactionId; }
    public void setTransactionId(UUID transactionId) { this.transactionId = transactionId; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}