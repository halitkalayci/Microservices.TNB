package com.example.identity.service.exception;

public class DuplicateTcknException extends RuntimeException {

    public DuplicateTcknException() {
        super("Bu TCKN ile kayıtlı bir kullanıcı zaten mevcut");
    }
}
