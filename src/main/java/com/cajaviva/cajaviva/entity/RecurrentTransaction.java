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
    @Column(name = "id")
    private UUID id;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "status")
    private Integer status;

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

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public RecurrentTransaction() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

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

    public BigDecimal getAmount() { return value; }
    public void setAmount(BigDecimal amount) { this.value = amount; }

    @Transient
    public UUID getAccountId() {
        return account != null ? account.getId() : null;
    }

    public void setAccountId(UUID accountId) {
        if (accountId == null) {
            this.account = null;
        } else {
            Account account = new Account();
            account.setId(accountId);
            this.account = account;
        }
    }

    @Transient
    public UUID getCategoryId() {
        return category != null ? category.getId() : null;
    }

    public void setCategoryId(UUID categoryId) {
        if (categoryId == null) {
            this.category = null;
        } else {
            Category category = new Category();
            category.setId(categoryId);
            this.category = category;
        }
    }
}
