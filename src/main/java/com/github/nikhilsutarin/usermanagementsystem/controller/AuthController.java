package com.github.nikhilsutarin.usermanagementsystem.controller;

import com.github.nikhilsutarin.usermanagementsystem.config.CustomUserDetails;
import com.github.nikhilsutarin.usermanagementsystem.dto.LoginRequest;
import com.github.nikhilsutarin.usermanagementsystem.dto.LoginResponse;
import com.github.nikhilsutarin.usermanagementsystem.jwt.JwtUtil;
import com.github.nikhilsutarin.usermanagementsystem.model.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        // Authenticate user with email/password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Get principal
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Generate JWT
        String jwt = jwtUtil.generateToken(userDetails.getUser());

        // Collect user roles
        Set<String> roles = userDetails.getUser().getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // Create response
        LoginResponse response = new LoginResponse(
                jwt,
                userDetails.getUser().getId(),
                userDetails.getUser().getName(),
                userDetails.getUser().getEmail(),
                roles
        );

        return ResponseEntity.ok(response);
    }

}

