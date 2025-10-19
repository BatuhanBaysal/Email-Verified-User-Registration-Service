package com.batuhan.project.controller;

import com.batuhan.project.dto.JwtResponse;
import com.batuhan.project.dto.LoginRequest;
import com.batuhan.project.dto.RegisterRequest;
import com.batuhan.project.jwt.JwtUtils;
import com.batuhan.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.saveNewUser(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getUsername()
        );
        return ResponseEntity.ok("Registration successful. Confirmation email is pending (Redis/Notification Service active).");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String email = authentication.getName();
        String jwt = jwtUtils.generateJwtToken(email);

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", email));
    }

    @GetMapping("/me")
    public ResponseEntity<String> getUserInfo(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok("You're logged in! Email: " + email + ". This is a protected resource.");
    }
}