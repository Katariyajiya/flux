package com.flux.controller;

import com.flux.entity.User;
import com.flux.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/login")
    public String login(@RequestParam String email,@RequestParam String password){
        User user = userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));

        if(user.getPassword() == null || !passwordEncoder.matches(password,user.getPassword())){
            throw new IllegalArgumentException("Invalid email or password");
        }
        return "Login successful";
    }
}
