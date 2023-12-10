package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.dto.ExpenseDto;
import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final Logger log = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<String> createExpense(@RequestBody ExpenseDto expenseDto){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String authenticatedUsername = authentication.getName();

            if (!expenseDto.getUsername().equals(authenticatedUsername)) {
                throw new Exception("You can only create expenses for yourself");
            }

            expenseService.createExpense(expenseDto);
            return ResponseEntity.ok("Expense created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable String expenseId){
        try {
            authenticateAndAuthorize(expenseId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

        expenseService.deleteExpense(expenseId);
        return ResponseEntity.ok("Successfully deleted the expense.");
    }

    @PutMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> updateExpense(@PathVariable String expenseId, @RequestBody ExpenseDto updatedExpenseDto){
        try {
            authenticateAndAuthorize(expenseId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

        // Update the expense
        try {
            expenseService.updateExpense(expenseId, updatedExpenseDto);
            return ResponseEntity.ok("Expense updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update expense");
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Expense> getExpenseByUsername(@RequestParam String username) {
        return expenseService.getExpensesByUsername(username);
    }

    private void authenticateAndAuthorize(String expenseId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        Expense existingExpense = expenseService.getExpenseById(expenseId);

        if (existingExpense == null) {
            throw new Exception("Expense not found");
        }

        if (!existingExpense.getUsername().equals(authenticatedUsername)) {
            throw new Exception("You do not have permission to update this expense");
        }
    }
}
