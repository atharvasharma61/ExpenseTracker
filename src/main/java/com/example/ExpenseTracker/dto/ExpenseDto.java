package com.example.ExpenseTracker.dto;

import com.example.ExpenseTracker.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExpenseDto {
    private String description;
    private BigDecimal amount;
    private String category;
    private Date date;
    private String username;
}
