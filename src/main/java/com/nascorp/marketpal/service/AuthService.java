package com.nascorp.marketpal.service;

import com.nascorp.marketpal.dto.RegisterRequest;
import com.nascorp.marketpal.entity.User;
import com.nascorp.marketpal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        // checks whether the username is already present or not
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        // checks if the email is already registered or not
        if(userRepository.existsByEmail(request.getEmail())) { 
            throw new RuntimeException("Email already registered");
        }

        // build user - pass get hashed here to secure it 
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .emailVerified(false)
                .enabled(true)
                .build();

        // user build - save it to the db
        userRepository.save(user);

        // *one remaining thing todo gen token store in cache(redis) and then send email
        return "Registered Successfully.Please check your email to verify your account";
    }

}
