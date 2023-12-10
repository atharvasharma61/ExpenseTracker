package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.controller.ExpenseController;
import com.example.ExpenseTracker.dto.ExpenseDto;
import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    @Autowired
    private ExpenseRepository expenseRepository;
    @Override
    public Expense createExpense(ExpenseDto expenseDto) {
        return expenseRepository.save(convertToEntity(expenseDto));
    }

    @Override
    public void deleteExpense(String expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    @Override
    public Expense updateExpense(String expenseId, ExpenseDto updatedExpenseDto) {
        Expense existingExpense = getExpenseById(expenseId);

        existingExpense.setDescription(updatedExpenseDto.getDescription());
        existingExpense.setAmount(updatedExpenseDto.getAmount());
        existingExpense.setCategory(updatedExpenseDto.getCategory());

        return expenseRepository.save(existingExpense);
    }

    @Override
    public List<Expense> getExpensesByUsername(String username) {
        return expenseRepository.findByUsername(username);
    }

    @Override
    public Expense getExpenseById(String expenseId) {
        Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);
        return expenseOptional.orElse(null);
    }

    private Expense convertToEntity(ExpenseDto expenseDto){
        return new Expense(expenseDto.getAmount(), expenseDto.getCategory(), expenseDto.getDate(), expenseDto.getUsername());
    }
}
