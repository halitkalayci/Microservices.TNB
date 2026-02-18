package com.example.identity.service.configuration;

import com.example.identity.service.util.EncryptionHelper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class CryptoConfiguration {

    @Bean
    public EncryptionHelper encryptionHelper(@Value("${app.crypto.aeskeyBase64}") String aesKeyBase64) {
        byte[] keyBytes = Base64.getDecoder().decode(aesKeyBase64);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        return new EncryptionHelper(key);
    }
}
