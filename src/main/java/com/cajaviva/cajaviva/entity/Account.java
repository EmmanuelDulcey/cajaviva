package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private UUID accountId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "account_type", nullable = false)
    private Integer accountType;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Account() {}

    // Getters y Setters
    public UUID getAccountId() { return accountId; }
    public void setAccountId(UUID accountId) { this.accountId = accountId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAccountType() { return accountType; }
    public void setAccountType(Integer accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}