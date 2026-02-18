package com.example.identity.service.exception;

public class InvalidResetTokenException extends RuntimeException {

    public InvalidResetTokenException() {
        super("Geçersiz veya süresi dolmuş şifre sıfırlama bağlantısı.");
    }

    public InvalidResetTokenException(String message) {
        super(message);
    }
}
