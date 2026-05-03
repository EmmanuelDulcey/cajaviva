package com.cajaviva.cajaviva.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "UserAccesses")
public class UserAccess {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "role", nullable = false)
    private Integer role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserAccess() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(UUID userId) {
        if (userId == null) {
            this.user = null;
        } else {
            this.user = new User(userId);
        }
    }
}
