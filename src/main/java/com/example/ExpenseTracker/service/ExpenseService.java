package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.dto.ExpenseDto;
import com.example.ExpenseTracker.model.Expense;

import java.util.List;

public interface ExpenseService {
    Expense createExpense(ExpenseDto expenseDto);
    void deleteExpense(String expenseId);
    Expense updateExpense(String expenseId, ExpenseDto updatedExpenseDto);
    List<Expense> getExpensesByUsername(String username);
    Expense getExpenseById(String expenseId);
}
