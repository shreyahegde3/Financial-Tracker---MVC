package com.financialtracker.controller;

import com.financialtracker.model.FinancialGoal;
import com.financialtracker.model.User;
import com.financialtracker.repository.FinancialGoalRepository;
import com.financialtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/goals")
public class FinancialGoalController {

    @Autowired
    private FinancialGoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showGoalsList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        
        List<FinancialGoal> goals = goalRepository.findByUserIdOrderByTargetDateAsc(user.getId());
        model.addAttribute("goals", goals);
        model.addAttribute("goal", new FinancialGoal());
        return "goals/list";
    }

    @PostMapping
    public String addGoal(@Valid @ModelAttribute("goal") FinancialGoal goal, BindingResult result) {
        if (result.hasErrors()) {
            return "goals/list";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        goal.setUser(user);
        goal.setCurrentAmount(BigDecimal.ZERO);
        goal.setStatus(FinancialGoal.GoalStatus.IN_PROGRESS);
        
        goalRepository.save(goal);
        return "redirect:/goals";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        
        FinancialGoal goal = goalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid goal ID: " + id));
            
        if (!goal.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized to edit this goal");
        }
        
        model.addAttribute("goal", goal);
        return "goals/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateGoal(@PathVariable Long id, @Valid @ModelAttribute("goal") FinancialGoal goal, BindingResult result) {
        if (result.hasErrors()) {
            return "goals/edit";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        
        FinancialGoal existingGoal = goalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid goal ID: " + id));
            
        if (!existingGoal.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized to edit this goal");
        }
        
        existingGoal.setName(goal.getName());
        existingGoal.setTargetAmount(goal.getTargetAmount());
        existingGoal.setTargetDate(goal.getTargetDate());
        
        goalRepository.save(existingGoal);
        return "redirect:/goals";
    }

    @PostMapping("/{id}/update-progress")
    public String updateProgress(@PathVariable Long id, @RequestParam BigDecimal amount) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        
        FinancialGoal goal = goalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid goal ID: " + id));
            
        if (!goal.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized to update this goal");
        }
        
        goal.setCurrentAmount(amount);
        
        // Update status if goal is reached
        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            goal.setStatus(FinancialGoal.GoalStatus.COMPLETED);
        }
        
        goalRepository.save(goal);
        return "redirect:/goals";
    }

    @PostMapping("/{id}/delete")
    public String deleteGoal(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        
        FinancialGoal goal = goalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid goal ID: " + id));
            
        if (!goal.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized to delete this goal");
        }
        
        goalRepository.delete(goal);
        return "redirect:/goals";
    }
} 