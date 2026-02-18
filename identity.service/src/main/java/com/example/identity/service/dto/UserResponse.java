package com.example.identity.service.dto;

import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String username;
    private String tckn;

    public UserResponse() {
    }

    public UserResponse(UUID id, String username, String tckn) {
        this.id = id;
        this.username = username;
        this.tckn = tckn;
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

    public String getTckn() {
        return tckn;
    }

    public void setTckn(String tckn) {
        this.tckn = tckn;
    }
}
