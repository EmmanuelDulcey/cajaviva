package com.cajaviva.cajaviva.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "RecurrentTransactions")
public class RecurrentTransaction {
    @Id
    @GeneratedValue
    @Column(name = "recurrent_transaction_id", columnDefinition = "uniqueidentifier")
    private UUID id;

    private double value;
    private LocalDate initialDate;
    private LocalDate endDate;
    private int frequency;
    private Integer customFrequency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // getters y setters
}
