package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.dto.ExpenseDto;
import com.example.ExpenseTracker.dto.UserDto;
import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.repository.UserRepository;
import com.example.ExpenseTracker.websecurity.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User registerUser(UserDto registrationDto) throws Exception {
        return userRepository.save(convertToEntity(registrationDto));
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
