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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) {
        // checks whether the username is already present or not
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        // checks if the email is already registered or not
        if(userRepository.existsByEmail(registerRequest.getEmail())) { 
            throw new RuntimeException("Email already registered");
        }

        // build user - pass get hashed here to secure it 
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .emailVerified(false)
                .enabled(true)
                .build();

        // user build - save it to the db
        userRepository.save(user);

        // *one remaining thing todo gen token store in cache(redis) and then send email
        return "Registered Successfully.Please check your email to verify your account";
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

        if(!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(token, user.getUsername(), "Login successfull");
    }
}
