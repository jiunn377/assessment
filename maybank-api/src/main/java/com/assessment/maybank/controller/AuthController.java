package com.assessment.maybank.controller;

import com.assessment.maybank.util.JwtUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "API authentication")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${assessment.auth.users.username}")
    private String username;

    @Value("${assessment.auth.users.password}")
    private String password;

    @PostMapping("/token")
    public ResponseEntity<String> login(
            @Parameter(description = "Username", example = "user")
            @RequestParam String username,
            @Parameter(description = "Password", example = "password")
            @RequestParam String password) {

        // Simple username/password check (for demo purposes)
        if (username.equals(username) && password.equals(password)) {
            String token = jwtUtils.generateToken(username);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}