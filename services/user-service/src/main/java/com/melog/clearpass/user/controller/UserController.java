package com.melog.clearpass.user.controller;

import com.melog.clearpass.user.model.User;
import com.melog.clearpass.user.service.UserService;
import com.melog.clearpass.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        logger.info("UserController: Creating user with username: {}", user.getUsername());
        userService.createUser(user);
        logger.info("UserController: User created successfully for username: {}", user.getUsername());
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getAllUsers() {
        logger.info("UserController: Getting all users");
        List<User> users = userService.getAllUsers();
        logger.info("UserController: Retrieved {} users", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("UserController: Getting user by ID: {}", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            // Return user without password for client applications
            User userWithoutPassword = user.get();
            userWithoutPassword.setPassword(null);
            logger.info("UserController: User found for ID: {}", id);
            return ResponseEntity.ok(userWithoutPassword);
        }
        logger.warn("UserController: User not found for ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/internal/username/{username}")
    public ResponseEntity<UserDto> getUserByUsernameInternal(@PathVariable String username, HttpServletRequest request) {
        logger.info("UserController: INTERNAL endpoint called for username: {}", username);
        logger.info("UserController: Request headers - X-Service-Token: {}", 
            request.getHeader("X-Service-Token") != null ? "present" : "missing");
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Map roles to Set<String>
            Set<String> roleNames = user.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet());
            UserDto dto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), roleNames);
            logger.info("UserController: UserDto for internal use: id={}, username={}, email={}, roles={}", dto.getId(), dto.getUsername(), dto.getEmail(), dto.getRoles());
            return ResponseEntity.ok(dto);
        } else {
            logger.warn("UserController: User not found for username: {}", username);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("UserController: Deleting user with ID: {}", id);
        userService.deleteUser(id);
        logger.info("UserController: User deleted successfully for ID: {}", id);
        return ResponseEntity.noContent().build();
    }
} 