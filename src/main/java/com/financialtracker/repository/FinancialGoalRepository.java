package com.financialtracker.repository;

import com.financialtracker.model.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    List<FinancialGoal> findByUserId(Long userId);
    List<FinancialGoal> findByUserIdOrderByTargetDateAsc(Long userId);
    List<FinancialGoal> findByUserIdAndStatus(Long userId, FinancialGoal.GoalStatus status);
} 