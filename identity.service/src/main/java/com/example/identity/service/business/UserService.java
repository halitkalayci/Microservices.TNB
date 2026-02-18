package com.example.identity.service.business;

import com.example.identity.service.entity.User;
import com.example.identity.service.repository.UserRepository;
import com.example.identity.service.util.EncryptionHelper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionHelper encryptionHelper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EncryptionHelper encryptionHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.encryptionHelper = encryptionHelper;
    }

    public void register(String username, String password, String tckn) {
        String encodedPassword = passwordEncoder.encode(password);
        String encryptedTckn = encryptTckn(tckn);
        System.out.println("Şifrelenmiş TCKN: " + encryptedTckn);
        //userRepository.save(new User(username, encodedPassword, encryptedTckn));
    }

    private String encryptTckn(String tckn) {
        try {
            byte[] encrypted = encryptionHelper.encrypt(tckn.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("TCKN şifreleme hatası", e);
        }
    }

    public void registerCrypted(String username, String encryptedPassword, String tckn) {
        userRepository.save(new User(username, encryptedPassword, tckn));
    }
}
