
package com.shopersa.backend_shopersa.MS_Auth.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey jwtSecret;
    private final long jwtExpirationMs;

    public JwtProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration:86400000}") long expiration
    ) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException(
                "La clave JWT debe tener al menos 32 bytes"
            );
        }

        if (expiration <= 0) {
            throw new IllegalArgumentException(
                "La duración del JWT debe ser mayor que cero"
            );
        }

        this.jwtSecret = Keys.hmacShaKeyFor(
            secret.getBytes(StandardCharsets.UTF_8)
        );

        this.jwtExpirationMs = expiration;
    }

    public String generateToken(Authentication authentication) {

        UserDetails userDetails =
            (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jwtSecret)
                .compact();
    }

    public String getUsernameFromJWT(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {

        try {
            Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(authToken);

            return true;

        } catch (Exception ex) {
            return false;
        }
    }
}
