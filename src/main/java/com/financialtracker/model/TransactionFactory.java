package com.financialtracker.model;

import java.time.LocalDate;

public class TransactionFactory {
    public static Transaction createTransaction(TransactionType type, Double amount, String description, LocalDate date, String category) {
        switch (type) {
            case INCOME:
                Income income = new Income();
                income.setAmount(amount);
                income.setSource(Income.IncomeSource.valueOf(description));
                income.setDate(date);
                income.setCategory(Income.IncomeCategory.valueOf(category));
                return income;
            case EXPENSE:
                Expense expense = new Expense();
                expense.setAmount(amount);
                expense.setDescription(description);
                expense.setDate(date);
                expense.setCategory(Expense.ExpenseCategory.valueOf(category));
                return expense;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + type);
        }
    }
    
    public enum TransactionType {
        INCOME,
        EXPENSE
    }
} 