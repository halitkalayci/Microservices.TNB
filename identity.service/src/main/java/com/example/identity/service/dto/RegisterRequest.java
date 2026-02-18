package com.example.identity.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Kullanıcı adı zorunludur")
    private String username;

    @NotBlank(message = "Şifre zorunludur")
    private String password;

    @NotBlank(message = "TCKN zorunludur")
    @Size(min = 11, max = 11, message = "TCKN 11 haneli olmalıdır")
    private String tckn;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String tckn) {
        this.username = username;
        this.password = password;
        this.tckn = tckn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTckn() {
        return tckn;
    }

    public void setTckn(String tckn) {
        this.tckn = tckn;
    }
}
