package com.example.identity.service.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Şifre en az 8 karakter olmalıdır ve en az bir büyük harf, bir küçük harf, bir sayı ve bir özel karakter içermelidir")
    private String password;

    @Column(nullable = false, length = 512)
    private String tckn;

    @Column(name = "tckn_hashed", nullable = false, unique = true, length = 64)
    private String tcknHashed;

    @Column(name = "password_reset_token", length = 64)
    private String passwordResetToken;

    @Column(name = "reset_token_valid_until")
    private Instant resetTokenValidUntil;

    public User() {
    }

    public User(String username, String password, String tckn, String tcknHashed) {
        this.username = username;
        this.password = password;
        this.tckn = tckn;
        this.tcknHashed = tcknHashed;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getTcknHashed() {
        return tcknHashed;
    }

    public void setTcknHashed(String tcknHashed) {
        this.tcknHashed = tcknHashed;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public Instant getResetTokenValidUntil() {
        return resetTokenValidUntil;
    }

    public void setResetTokenValidUntil(Instant resetTokenValidUntil) {
        this.resetTokenValidUntil = resetTokenValidUntil;
    }
}
