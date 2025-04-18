package com.financialtracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private ExpenseCategory category;

    @NotNull
    @Positive(message = "Limit amount must be positive")
    @Column(name = "limit_amount")
    private Double limitAmount;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Budget() {
        // Public constructor for JPA
    }

    public static class Builder {
        private User user;
        private ExpenseCategory category;
        private Double limitAmount;
        private LocalDate startDate;
        private LocalDate endDate;

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder category(ExpenseCategory category) {
            this.category = category;
            return this;
        }

        public Builder limitAmount(Double limitAmount) {
            this.limitAmount = limitAmount;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Budget build() {
            Budget budget = new Budget();
            budget.user = this.user;
            budget.category = this.category;
            budget.limitAmount = this.limitAmount;
            budget.startDate = this.startDate;
            budget.endDate = this.endDate;
            return budget;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Using the same categories as Expense for consistency
    public enum ExpenseCategory {
        HOUSING("Housing"),
        TRANSPORTATION("Transportation"),
        FOOD("Food"),
        UTILITIES("Utilities"),
        HEALTHCARE("Healthcare"),
        ENTERTAINMENT("Entertainment"),
        EDUCATION("Education"),
        SHOPPING("Shopping"),
        DEBT("Debt"),
        SAVINGS("Savings"),
        OTHER("Other");

        private final String displayName;

        ExpenseCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
} 