package com.financialtracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "income")
public class Income implements Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "income_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private IncomeSource source;

    @NotNull
    @Enumerated(EnumType.STRING)
    private IncomeCategory category;

    @NotNull
    private LocalDate date;

    @NotNull
    @Column(name = "is_recurring")
    private boolean recurring;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public String getDescription() {
        return source.toString();
    }

    @Override
    public String getCategory() {
        return category.toString();
    }

    public enum IncomeSource {
        SALARY, FREELANCE, INVESTMENTS, BUSINESS, OTHER
    }

    public enum IncomeCategory {
        FIXED, VARIABLE
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 