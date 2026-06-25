package com.nascorp.marketpal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nascorp.marketpal.dto.AuthResponse;
import com.nascorp.marketpal.dto.LoginRequest;
import com.nascorp.marketpal.dto.RegisterRequest;
import com.nascorp.marketpal.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register
    (@Valid @ RequestBody RegisterRequest request) { 
        String message = authService.register(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/test-protected")
    public ResponseEntity<?> testProtected(HttpServletRequest request) {
        
        String username = SecurityContextHolder.getContext()
                          .getAuthentication()
                          .getName();
        return ResponseEntity.ok(Map.of("message", "You are authenticated!", "username", username));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    } catch (RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
  }
}
