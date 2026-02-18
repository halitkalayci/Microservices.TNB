package com.example.identity.service.business;

import com.example.identity.service.dto.UserResponse;
import com.example.identity.service.entity.User;
import com.example.identity.service.exception.DuplicateTcknException;
import com.example.identity.service.repository.UserRepository;
import com.example.identity.service.util.EncryptionHelper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionHelper encryptionHelper;
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        try {
            User user = new User();
            user.setId(UUID.fromString(rs.getString("id")));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setTckn(rs.getString("tckn"));
            user.setTcknHashed(rs.getString("tckn_hashed"));
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EncryptionHelper encryptionHelper, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.encryptionHelper = encryptionHelper;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Id'ye göre kullanıcı bulur. SQL Injection'a bilerek açıktır (string birleştirme kullanılır).
     */
    public List<User> findByIdRaw(String id) {
        String sql = "SELECT * FROM users WHERE id = '" + id + "'";
        List<User> users = jdbcTemplate.query(sql, USER_ROW_MAPPER);
        return users;
    }

    // Parametreli SQL Injection'a karşı önlem alınmalı.
    public List<User> findByIdPreparedStatement(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[] { id }, USER_ROW_MAPPER);
        return users;
    }

    public void register(String username, String password, String tckn) {
        String tcknHashed = hashTckn(tckn);
        if (userRepository.existsByTcknHashed(tcknHashed)) {
            throw new DuplicateTcknException();
        }
        String encodedPassword = passwordEncoder.encode(password);
        String encryptedTckn = encryptTckn(tckn);
        userRepository.save(new User(username, encodedPassword, encryptedTckn, tcknHashed));
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
        String tcknHashed = hashTckn(tckn);
        if (userRepository.existsByTcknHashed(tcknHashed)) {
            throw new DuplicateTcknException();
        }
        userRepository.save(new User(username, encryptedPassword, tckn, tcknHashed));
    }

    private String hashTckn(String tckn) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(tckn.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("TCKN hash hesaplanamadı", e);
        }
    }

    private String decryptTckn(String encryptedTckn) {
        try {
            byte[] encrypted = Base64.getDecoder().decode(encryptedTckn);
            byte[] decrypted = encryptionHelper.decrypt(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // registerCrypted ile kaydedilenler şifresiz olabilir
            return encryptedTckn;
        }
    }

    public List<UserResponse> findAllWithDecryptedTckn() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        decryptTckn(user.getTckn())
                ))
                .toList();
    }
}
