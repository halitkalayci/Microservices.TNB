package com.example.identity.service.controller;

import com.example.identity.service.business.UserService;
import com.example.identity.service.dto.RegisterRequest;
import com.example.identity.service.dto.UserResponse;
import com.example.identity.service.exception.DuplicateTcknException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.identity.service.entity.User;

import java.util.List;
import java.util.Map;

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

    @GetMapping
    @Operation(summary = "Kullanıcı listesi", description = "Tüm kullanıcıları TCKN değerleri çözülmüş olarak listeler")
    public List<UserResponse> listUsers() {
        return userService.findAllWithDecryptedTckn();
    }

    @GetMapping("/raw/{id}")
    @Operation(summary = "Id ile kullanıcı (raw)", description = "JdbcTemplate ile id'ye göre kullanıcı döner. SQL Injection'a açıktır.")
    public ResponseEntity<List<User>> getUserByIdRaw(@PathVariable String id) {
        List<User> users = userService.findByIdRaw(id);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/prepared/{id}")
    @Operation(summary = "Id ile kullanıcı (raw)", description = "JdbcTemplate ile id'ye göre kullanıcı döner. SQL Injection'a kapalıdır.")
    public ResponseEntity<List<User>> preparedStatement(@PathVariable String id) {
        // Kullanıcı girdisi asla alınmamalı.
        //String path = "C:\\; rm -rf *";
        //Process p = Runtime.getRuntime().exec(  "cd " + path);

        List<User> users = userService.findByIdPreparedStatement(id);
        return ResponseEntity.ok(users);
    }
    @ExceptionHandler(DuplicateTcknException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDuplicateTckn(DuplicateTcknException ex) {
        return Map.of("message", ex.getMessage());
    }
}
