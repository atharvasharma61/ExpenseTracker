package com.example.ExpenseTracker.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@ConfigurationProperties(prefix = "app.jwt")
@Configuration
@Getter
@Setter
public class JwtConfig {
    private String secretKey;
    private long validityInMs;
    public SecretKey getSecretKeyForSigning() {
        // Use Keys.secretKeyFor to generate a secure key for HS512
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}
