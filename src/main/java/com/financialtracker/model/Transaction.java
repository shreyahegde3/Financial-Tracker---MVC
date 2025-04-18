package com.financialtracker.model;

import java.time.LocalDate;

public interface Transaction {
    Double getAmount();
    String getDescription();
    LocalDate getDate();
    String getCategory();
} 