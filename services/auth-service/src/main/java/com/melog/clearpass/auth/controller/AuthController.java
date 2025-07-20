package com.melog.clearpass.auth.controller;

import com.melog.clearpass.auth.dto.LoginRequest;
import com.melog.clearpass.auth.dto.LoginResponse;
import com.melog.clearpass.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("AuthController: Received login request for username: {}", loginRequest.getUsername());
        try {
            LoginResponse response = authService.login(loginRequest);
            logger.info("AuthController: Login successful for username: {}", loginRequest.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("AuthController: Login failed for username: {}. Error: {}", loginRequest.getUsername(), e.getMessage(), e);
            throw e;
        }
    }
} 