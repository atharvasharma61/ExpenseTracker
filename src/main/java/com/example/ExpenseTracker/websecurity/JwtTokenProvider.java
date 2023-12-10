package com.example.ExpenseTracker.websecurity;

import com.example.ExpenseTracker.config.JwtConfig;
import com.example.ExpenseTracker.model.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long validityInMs;
    private final JwtConfig jwtConfig;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.secretKey = jwtConfig.getSecretKeyForSigning();
        this.validityInMs = jwtConfig.getValidityInMs();
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 3600 * 1000))
                .signWith(SignatureAlgorithm.HS512, this.secretKey).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        final Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}