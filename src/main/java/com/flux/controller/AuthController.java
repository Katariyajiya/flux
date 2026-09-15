package com.flux.controller;

import com.flux.dto.AuthResponse;
import com.flux.dto.LoginRequest;
import com.flux.dto.RegisterRequest;
import com.flux.dto.RegisterResponse;
import com.flux.entity.User;
import com.flux.entity.UserStatus;
import com.flux.repository.UserRepository;
import com.flux.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));

        if (user.getPassword() == null ||
                !passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword())) {

            throw new IllegalArgumentException("Invalid email or password");
        }

        if (user.getStatus() == UserStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Account is pending admin approval"
            );
        }

        if (user.getStatus() == UserStatus.DISABLED) {
            throw new IllegalArgumentException(
                    "Account is disabled"
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        AuthResponse response = new AuthResponse(
                "Login successful",
                token,
                user.getId(),
                user.getRole() != null
                        ? user.getRole().name()
                        : null,
                user.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setStatus(UserStatus.PENDING);
        user.setRole(null);
        user.setDepartment(null);

        userRepository.save(user);

        RegisterResponse response = new RegisterResponse(
                "Registration successful. Waiting for admin approval.",
                user.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }
}
