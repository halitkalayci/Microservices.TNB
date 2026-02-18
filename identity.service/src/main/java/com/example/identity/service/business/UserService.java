package com.example.identity.service.business;

import com.example.identity.service.entity.User;
import com.example.identity.service.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password, String tckn) {
        String encodedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(username, encodedPassword, tckn));
    }

    public void registerCrypted(String username, String encryptedPassword, String tckn) {
        userRepository.save(new User(username, encryptedPassword, tckn));
    }
}
