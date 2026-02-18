package com.example.identity.service.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {

    @NotBlank(message = "Kullanıcı adı zorunludur")
    private String username;

    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
