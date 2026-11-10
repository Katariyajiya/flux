package com.flux.controller;

import com.flux.dto.FcmTokenRequest;
import com.flux.entity.User;
import com.flux.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/fcm-token")
    public ResponseEntity<?> updateFcmToken(
            @RequestBody FcmTokenRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity
                    .status(401)
                    .body("Authentication required");
        }

        if (request.getToken() == null || request.getToken().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("FCM token is required");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        user.setFcmToken(request.getToken());
        userRepository.save(user);

        System.out.println(
                ">>> FCM TOKEN UPDATED FOR USER: " + user.getEmail()
        );

        return ResponseEntity.ok(
                Map.of("message", "FCM token registered successfully")
        );
    }
}