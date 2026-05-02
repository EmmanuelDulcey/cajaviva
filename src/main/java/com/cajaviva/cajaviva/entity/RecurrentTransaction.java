package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recurrent_transactions")
public class RecurrentTransaction {

    @Id
    @GeneratedValue
    @Column(name = "recurrent_transaction_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "initial_date", nullable = false)
    private LocalDate initialDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "frequency", nullable = false)
    private Integer frequency;

    @Column(name = "custom_frequency")
    private Integer customFrequency;

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

    public RecurrentTransaction() {}

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public LocalDate getInitialDate() { return initialDate; }
    public void setInitialDate(LocalDate initialDate) { this.initialDate = initialDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getFrequency() { return frequency; }
    public void setFrequency(Integer frequency) { this.frequency = frequency; }

    public Integer getCustomFrequency() { return customFrequency; }
    public void setCustomFrequency(Integer customFrequency) { this.customFrequency = customFrequency; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Object getAmount() {
        throw new UnsupportedOperationException("Unimplemented method 'getAmount'");
    }

    public void setAmount(Object amount) {
        throw new UnsupportedOperationException("Unimplemented method 'setAmount'");
    }

    public Object getStatus() {
        throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
    }

    public void setStatus(Object status) {
        throw new UnsupportedOperationException("Unimplemented method 'setStatus'");
    }

    public void setAccountId(UUID object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAccountId'");
    }

    public void setCategoryId(UUID object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCategoryId'");
    }

    public Object getAccountId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountId'");
    }

    public Object getCategoryId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoryId'");
    }
}
