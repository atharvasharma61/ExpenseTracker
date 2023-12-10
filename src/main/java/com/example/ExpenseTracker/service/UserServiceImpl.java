package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.dto.ExpenseDto;
import com.example.ExpenseTracker.dto.UserDto;
import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.repository.UserRepository;
import com.example.ExpenseTracker.websecurity.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        User user = getUserByUsername(registrationDto.getUsername());
        if(user != null)
            throw new Exception("Username is not available.");

        return userRepository.save(convertToEntity(registrationDto));
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(String username) {
        User user = getUserByUsername(username);
        Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
        logger.info("user: " + user);
        if (user == null){
            throw new UsernameNotFoundException("User does not exist");
        }
        userRepository.delete(user);
    }

    private User convertToEntity(UserDto userDto){
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        return new User(userDto.getName(), userDto.getUsername(), encodedPassword);
    }

}
