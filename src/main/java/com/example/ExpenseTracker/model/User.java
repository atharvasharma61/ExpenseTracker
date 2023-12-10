package com.example.ExpenseTracker.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private final String username;
    @NonNull
    private String password;
}
