package com.example.ExpenseTracker.model;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "expenses")
@Data
public class Expense {
    @Id
    private String id;
    private String description;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String category;
    @NonNull
    private Date date;
    @NonNull
    private String username;
}
