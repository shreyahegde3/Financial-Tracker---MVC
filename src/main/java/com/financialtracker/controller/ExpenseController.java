package com.financialtracker.controller;

import com.financialtracker.model.Expense;
import com.financialtracker.model.User;
import com.financialtracker.repository.ExpenseRepository;
import com.financialtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showExpenseList(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(auth.getName()).orElseThrow();
            
            List<Expense> expenses = expenseRepository.findByUserIdOrderByDateDesc(user.getId());
            model.addAttribute("expenses", expenses);
            model.addAttribute("expense", new Expense());
            model.addAttribute("expenseCategories", Expense.ExpenseCategory.values());
            return "expenses/list";
        } catch (Exception e) {
            logger.error("Error in showExpenseList: ", e);
            throw e;
        }
    }

    @PostMapping
    public String addExpense(@Valid @ModelAttribute("expense") Expense expense,
                           @RequestParam("category") String categoryStr,
                           BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                logger.error("Validation errors: {}", result.getAllErrors());
                // Add the expenses list back to the model
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = userRepository.findByEmail(auth.getName()).orElseThrow();
                List<Expense> expenses = expenseRepository.findByUserIdOrderByDateDesc(user.getId());
                model.addAttribute("expenses", expenses);
                model.addAttribute("expenseCategories", Expense.ExpenseCategory.values());
                return "expenses/list";
            }

            try {
                expense.setCategory(Expense.ExpenseCategory.valueOf(categoryStr));
            } catch (IllegalArgumentException e) {
                result.rejectValue("category", "error.category", "Invalid category");
                return "expenses/list";
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(auth.getName()).orElseThrow();
            expense.setUser(user);
            
            // Set today's date if no date was provided
            if (expense.getDate() == null) {
                expense.setDate(LocalDate.now());
            }
            
            logger.info("Saving expense: {}", expense);
            expenseRepository.save(expense);
            return "redirect:/expenses";
        } catch (Exception e) {
            logger.error("Error in addExpense: ", e);
            // Add the expenses list back to the model
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(auth.getName()).orElseThrow();
            List<Expense> expenses = expenseRepository.findByUserIdOrderByDateDesc(user.getId());
            model.addAttribute("expenses", expenses);
            model.addAttribute("expenseCategories", Expense.ExpenseCategory.values());
            model.addAttribute("error", "Failed to add expense: " + e.getMessage());
            return "expenses/list";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Expense expense = expenseRepository.findById(id).orElseThrow();
            model.addAttribute("expense", expense);
            model.addAttribute("expenseCategories", Expense.ExpenseCategory.values());
            return "expenses/edit";
        } catch (Exception e) {
            logger.error("Error in showEditForm: ", e);
            throw e;
        }
    }

    @PostMapping("/{id}/edit")
    public String updateExpense(@PathVariable Long id, 
                              @Valid @ModelAttribute("expense") Expense expense,
                              @RequestParam("category") String categoryStr,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "expense/edit";
        }

        try {
            Expense.ExpenseCategory category = Expense.ExpenseCategory.valueOf(categoryStr);
            
            Expense existingExpense = expenseRepository.findById(id).orElseThrow();
            existingExpense.setAmount(expense.getAmount());
            existingExpense.setCategory(category);
            existingExpense.setDate(expense.getDate());
            existingExpense.setRecurring(expense.isRecurring());

            expenseRepository.save(existingExpense);
            return "redirect:/expenses";
        } catch (IllegalArgumentException e) {
            result.rejectValue("category", "error.category", "Invalid category");
            return "expense/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteExpense(@PathVariable Long id) {
        expenseRepository.deleteById(id);
        return "redirect:/expenses";
    }
} 