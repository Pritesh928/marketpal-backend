package com.nascorp.marketpal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nascorp.marketpal.dto.AuthResponse;
import com.nascorp.marketpal.dto.LoginRequest;
import com.nascorp.marketpal.dto.RegisterRequest;
import com.nascorp.marketpal.service.AuthService;
import com.nascorp.marketpal.service.EmailVerificationService;
import com.nascorp.marketpal.repository.UserRepository;
import com.nascorp.marketpal.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public ResponseEntity<String> register
    (@Valid @ RequestBody RegisterRequest request) { 
        String message = authService.register(request);
        return ResponseEntity.ok(message);
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

  @GetMapping("/verify-email")
  public ResponseEntity<?> verifyEmail(@RequestParam String token) {
    try {
        String email = emailVerificationService.verifyToken(token);

        User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Email verified successfully you can now login."));
    } catch (RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
  }
}
