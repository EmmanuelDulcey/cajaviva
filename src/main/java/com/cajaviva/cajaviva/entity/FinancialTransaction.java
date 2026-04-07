package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "FinancialTransactions")
public class FinancialTransaction {
    @Id
    @GeneratedValue
    @Column(name = "transaction_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    private int type;
    private double value;
    private String description;
    private LocalDateTime date;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // getters y setters
}
