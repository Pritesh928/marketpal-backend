package com.nascorp.marketpal.service;

import com.nascorp.marketpal.dto.AuthResponse;
import com.nascorp.marketpal.dto.LoginRequest;
import com.nascorp.marketpal.dto.RegisterRequest;
import com.nascorp.marketpal.entity.User;
import com.nascorp.marketpal.repository.UserRepository;
import com.nascorp.marketpal.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword())).emailVerified(true)
                .enabled(true)
                .build();
            
        userRepository.save(user);

        return "Registered Successfully!!";
    }

    public AuthResponse login(LoginRequest loginRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        )
    );

    User user = userRepository.findByUsername(loginRequest.getUsername())
           .orElseThrow(() -> new RuntimeException("User not found"));

    String token = jwtService.generateToken(user.getUsername());

    return new AuthResponse(token, user.getUsername(), "Login successful");
    }  
}