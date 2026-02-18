package com.example.identity.service.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

// IV kullanarak verilen veriyi şifreleme, şifreyi çözme.
// Şifreleme ve çözme işlemleri taglenerek doğruluğu sağlanmalı.
public class EncryptionHelper {
    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;
    private final SecretKey key;
    private final SecureRandom secureRandom = new SecureRandom();

    public EncryptionHelper(SecretKey key) {
        this.key = key;
    }

    public byte[] encrypt(byte[] plaintext) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        secureRandom.nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_BITS, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
        byte[] ciphertext = cipher.doFinal(plaintext);
        byte[] result = new byte[IV_SIZE + ciphertext.length];
        System.arraycopy(iv, 0, result, 0, IV_SIZE);
        System.arraycopy(ciphertext, 0, result, IV_SIZE, ciphertext.length);
        return result;
    }

    public byte[] decrypt(byte[] encrypted) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(encrypted, 0, iv, 0, IV_SIZE);
        byte[] ciphertext = new byte[encrypted.length - IV_SIZE];
        System.arraycopy(encrypted, IV_SIZE, ciphertext, 0, ciphertext.length);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_BITS, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
        return cipher.doFinal(ciphertext);
    }
}
