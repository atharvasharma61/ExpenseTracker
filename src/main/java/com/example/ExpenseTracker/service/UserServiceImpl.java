package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.dto.ExpenseDto;
import com.example.ExpenseTracker.dto.UserDto;
import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User registerUser(UserDto registrationDto) throws Exception {
        // Check if the username is already taken
        if (userRepository.findByUsername(registrationDto.getUsername()) != null) {
            throw new Exception("Username is already taken");
        }
        // Save the user to the repository
        return userRepository.save(convertToEntity(registrationDto));
    }

    @Override
    public User loginUser(UserDto loginDto) throws Exception {
        User user = getUserByUsername(loginDto.getUsername());

        if (user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new Exception("Invalid username or password");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(String userName) {
        User user = getUserByUsername(userName);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    private User convertToEntity(UserDto userDto){
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        return new User(userDto.getName(), userDto.getUsername(), encodedPassword);
    }

}
