package com.flux.dto;

public class AuthResponse {

    private String message;
    private String token;
    private Long userId;
    private String role;
    private String status;

    public AuthResponse(
            String message,
            String token,
            Long userId,
            String role,
            String status) {

        this.message = message;
        this.token = token;
        this.userId = userId;
        this.role = role;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }
}