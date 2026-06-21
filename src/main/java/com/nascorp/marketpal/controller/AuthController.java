package com.nascorp.marketpal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nascorp.marketpal.dto.RegisterRequest;
import com.nascorp.marketpal.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
}
