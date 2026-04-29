package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {

    @Id
    @GeneratedValue
    @Column(name = "transaction_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relación con Account
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Relación con Category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public FinancialTransaction() {}

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
