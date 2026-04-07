package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserAccesses")
public class UserAccess {
    @Id
    @GeneratedValue
    @Column(name = "user_access_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    private int role;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // getters y setters
}
