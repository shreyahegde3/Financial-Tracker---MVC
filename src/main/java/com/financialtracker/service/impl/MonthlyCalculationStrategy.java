package com.financialtracker.service.impl;

import com.financialtracker.service.FinancialCalculationStrategy;
import com.financialtracker.model.Transaction;
import java.time.LocalDate;
import java.util.List;

public class MonthlyCalculationStrategy implements FinancialCalculationStrategy {
    @Override
    public double calculate(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
            .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
} 