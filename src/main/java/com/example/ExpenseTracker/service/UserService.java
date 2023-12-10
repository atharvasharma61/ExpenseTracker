package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.dto.UserDto;
import com.example.ExpenseTracker.model.User;

public interface UserService {
    User registerUser(UserDto registrationDto) throws Exception;

    User getUserByUsername(String username);

    void deleteUser(String userName);
}
