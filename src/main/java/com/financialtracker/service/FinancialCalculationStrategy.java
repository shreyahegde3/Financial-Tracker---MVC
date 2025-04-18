package com.financialtracker.service;

import java.time.LocalDate;
import java.util.List;
import com.financialtracker.model.Transaction;

public interface FinancialCalculationStrategy {
    double calculate(List<Transaction> transactions, LocalDate startDate, LocalDate endDate);
} 