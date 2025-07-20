package com.melog.clearpass.auth.service;

import com.melog.clearpass.auth.dto.LoginRequest;
import com.melog.clearpass.auth.dto.LoginResponse;
import com.melog.clearpass.auth.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Value("${user.service.url:http://user-service:8081}")
    private String userServiceUrl;
    
    @Value("${user.service.m2m.token}")
    private String m2mToken;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
        this.restTemplate = new RestTemplate();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        logger.info("AuthService: Login attempt for username: {}", loginRequest.getUsername());
        
        try {
            logger.info("AuthService: Calling getUserByUsername for username: {}", loginRequest.getUsername());
            // Get user from user-service using M2M token
            UserDto user = getUserByUsername(loginRequest.getUsername());
            
            if (user == null) {
                logger.warn("AuthService: User not found for username: {}", loginRequest.getUsername());
                throw new RuntimeException("Invalid credentials");
            }

            logger.info("AuthService: User found, validating password for username: {}", loginRequest.getUsername());

            // Validate password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                logger.warn("AuthService: Invalid password for username: {}", loginRequest.getUsername());
                throw new RuntimeException("Invalid credentials");
            }

            logger.info("AuthService: Password valid, generating JWT for username: {}", loginRequest.getUsername());

            // Generate JWT token
            String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRoles());
            
            logger.info("AuthService: JWT generated for username: {}", loginRequest.getUsername());
            
            return new LoginResponse(token);
        } catch (Exception e) {
            logger.error("AuthService: Error during login for username: {}. Error: {}", loginRequest.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    private UserDto getUserByUsername(String username) {
        logger.info("Attempting to get user by username: {}", username);
        logger.info("userServiceUrl value: '{}' (length: {})", userServiceUrl, userServiceUrl.length());
        logger.info("M2M token being used: {}", (m2mToken != null && m2mToken.length() > 6) ? m2mToken.substring(0,3) + "..." + m2mToken.substring(m2mToken.length()-3) : m2mToken);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Service-Token", m2mToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String url = userServiceUrl + "/api/users/internal/username/" + username;
            logger.info("Calling user-service URL: '{}'", url);
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );
            logger.info("User-service raw response status: {}", rawResponse.getStatusCode());
            logger.info("User-service raw response body: {}", rawResponse.getBody());
            // Now try to deserialize
            try {
                UserDto user = new com.fasterxml.jackson.databind.ObjectMapper().readValue(rawResponse.getBody(), UserDto.class);
                logger.info("User-service response successfully deserialized to UserDto");
                return user;
            } catch (Exception ex) {
                logger.error("Error deserializing user-service response to UserDto: {}", ex.getMessage(), ex);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error calling user-service for username: {}", username, e);
            return null;
        }
    }
} 