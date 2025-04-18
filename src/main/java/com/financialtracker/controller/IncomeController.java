package com.financialtracker.controller;

import com.financialtracker.model.Income;
import com.financialtracker.model.User;
import com.financialtracker.repository.IncomeRepository;
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

@Controller
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showIncomeList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        
        List<Income> incomes = incomeRepository.findByUserIdOrderByDateDesc(user.getId());
        model.addAttribute("incomes", incomes);
        model.addAttribute("income", new Income());
        model.addAttribute("incomeSources", Income.IncomeSource.values());
        model.addAttribute("incomeCategories", Income.IncomeCategory.values());
        return "income/list";
    }

    @PostMapping
    public String addIncome(@Valid @ModelAttribute("income") Income income, 
                          @RequestParam("source") String sourceStr,
                          @RequestParam("category") String categoryStr,
                          BindingResult result) {
        if (result.hasErrors()) {
            return "income/list";
        }

        try {
            income.setSource(Income.IncomeSource.valueOf(sourceStr));
            income.setCategory(Income.IncomeCategory.valueOf(categoryStr));
        } catch (IllegalArgumentException e) {
            result.rejectValue("source", "error.source", "Invalid source or category");
            return "income/list";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        income.setUser(user);
        
        // Set today's date if no date was provided
        if (income.getDate() == null) {
            income.setDate(LocalDate.now());
        }
        
        incomeRepository.save(income);
        return "redirect:/income";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Income income = incomeRepository.findById(id).orElseThrow();
        model.addAttribute("income", income);
        model.addAttribute("incomeSources", Income.IncomeSource.values());
        model.addAttribute("incomeCategories", Income.IncomeCategory.values());
        return "income/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateIncome(@PathVariable Long id, 
                             @Valid @ModelAttribute("income") Income income,
                             @RequestParam("source") String sourceStr,
                             @RequestParam("category") String categoryStr,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "income/edit";
        }

        try {
            Income.IncomeSource source = Income.IncomeSource.valueOf(sourceStr);
            Income.IncomeCategory category = Income.IncomeCategory.valueOf(categoryStr);
            
            Income existingIncome = incomeRepository.findById(id).orElseThrow();
            existingIncome.setAmount(income.getAmount());
            existingIncome.setSource(source);
            existingIncome.setCategory(category);
            existingIncome.setDate(income.getDate());
            existingIncome.setRecurring(income.isRecurring());

            incomeRepository.save(existingIncome);
            return "redirect:/income";
        } catch (IllegalArgumentException e) {
            result.rejectValue("source", "error.source", "Invalid source or category");
            return "income/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteIncome(@PathVariable Long id) {
        incomeRepository.deleteById(id);
        return "redirect:/income";
    }
} 