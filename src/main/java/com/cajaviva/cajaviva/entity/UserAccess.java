package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_access")
public class UserAccess {

    @Id
    private UUID id;

    @Column(name = "person_id")
    private UUID personId;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "role")
    private String role;

    @Column(name = "user_id")
    private UUID userId;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPersonId() { return personId; }
    public void setPersonId(UUID personId) { this.personId = personId; }

    public UUID getAccountId() { return accountId; }
    public void setAccountId(UUID accountId) { this.accountId = accountId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
}
