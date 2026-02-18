package com.example.identity.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordConfirmRequest {

    @NotBlank(message = "Token zorunludur")
    private String token;

    @NotBlank(message = "Yeni şifre zorunludur")
    @JsonProperty("new-password")
    private String newPassword;

    public ResetPasswordConfirmRequest() {
    }

    public ResetPasswordConfirmRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
