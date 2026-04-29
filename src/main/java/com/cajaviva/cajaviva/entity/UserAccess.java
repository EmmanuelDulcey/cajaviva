package com.cajaviva.cajaviva.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserAccess {

    private UUID id;
    private Integer role;
    private LocalDateTime createdAt;

    //SOLO IDs 
    private UUID accountId;
    private UUID userId;

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

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}