package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Entity
@Table(name = "user")
@Data
public class User {
    private static final Logger log = LoggerFactory.getLogger(User.class);
    @Id
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "password_hash", length = 32, nullable = false)
    private String passwordHash;
    @Column(length = 12, nullable = false)
    private String salt;

    public User(){

    }

    public User(String name,String email, String plainPassword) {
        this.name = name;
        this.email = email;
        this.salt = generateSalt();
        this.passwordHash = hashPassword(plainPassword, this.salt);
    }

    private String generateSalt() {
        byte[] saltBytes = new byte[8];
        new SecureRandom().nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest((password + salt).getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 哈希失败", e);
            return null;
        }
    }

    public boolean checkPassword(String inputPassword) {
        if (this.salt == null || this.passwordHash == null) {
            log.warn("用户的 salt 或 passwordHash 为 null： {}", this.name);
            return false;
        }
        return this.passwordHash.equals(hashPassword(inputPassword, this.salt));
    }

}

