package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.dto.UserDto;
import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.service.UserService;
import com.example.ExpenseTracker.websecurity.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.remote.JMXAuthenticator;

@RequestMapping("/api/users")
@Validated
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok("Registration successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Registration failed: " + e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto userDto) {
        try{
            authenticate(userDto.getUsername(), userDto.getPassword());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }

        String token = jwtTokenProvider.generateToken(userDto.getUsername());
        return ResponseEntity.ok(token);
    }

    @ResponseBody
    @DeleteMapping
    @Secured("ROLE_USER")
    public ResponseEntity<String> deleteUser(@RequestParam String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok("Successfully deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to delete User: " + e.getMessage());
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
