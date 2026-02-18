package com.example.identity.service.controller;

import com.example.identity.service.business.UserService;
import com.example.identity.service.dto.ResetPasswordConfirmRequest;
import com.example.identity.service.dto.ResetPasswordRequest;
import com.example.identity.service.exception.InvalidResetTokenException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Password Reset", description = "Şifre sıfırlama API")
public class PasswordResetController {

    private final UserService userService;

    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Şifre sıfırlama talebi", description = "Kullanıcı adı ile şifre sıfırlama token'ı oluşturulur (15 dakika geçerli)")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.requestPasswordReset(request.getUsername());
    }

    @PostMapping("/reset-password-confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Şifre sıfırlama onayı", description = "Geçerli token ve new-password ile kullanıcı şifresi güncellenir")
    public void resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmRequest request) {
        userService.confirmPasswordReset(request.getToken(), request.getNewPassword());
    }

    @ExceptionHandler(InvalidResetTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidResetToken(InvalidResetTokenException ex) {
        return Map.of("message", ex.getMessage());
    }
}
