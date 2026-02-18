package com.example.identity.service.controller;

import com.example.identity.service.business.UserService;
import com.example.identity.service.dto.RegisterRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Kullanıcı kayıt API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Kayıt", description = "Şifre sunucuda BCrypt ile hash'lenerek kaydedilir")
    public void register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request.getUsername(), request.getPassword(), request.getTckn());
    }

    @PostMapping("/register-crypted")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Şifreli kayıt", description = "Şifre istemci tarafında önceden hash'lenmiş olarak kabul edilir")
    public void registerCrypted(@Valid @RequestBody RegisterRequest request) {
        userService.registerCrypted(request.getUsername(), request.getPassword(), request.getTckn());
    }
}
