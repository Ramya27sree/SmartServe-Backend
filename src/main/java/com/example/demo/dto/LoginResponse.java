package com.example.demo.dto;

public class LoginResponse {

    private String message;
    private String token;
    private String tokenType;
    private String role;
    private int userId;
    private String name;

    public LoginResponse(
            String message,
            String token,
            String role,
            int userId,
            String name) {

        this.message = message;
        this.token = token;
        this.tokenType = "Bearer";
        this.role = role;
        this.userId = userId;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRole() {
        return role;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}