package com.shopersa.backend_shopersa.MS_Auth.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final String jwtSecret = "MiClaveSecretaSuperLargaYComplejaParaElTokenJWTQueNadieDebeAdivinar123456789";
    private final long jwtExpirationMs = 86400000;

    public String generateToken(Authentication authentication) {
 
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String username = userDetails.getUsername();


        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token) 
                .getPayload();
        
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            System.out.println("Validación de token: ÉXITO");
            return true;
        } catch (Exception ex) {
            System.err.println("Validación de token fallida: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            return false;
        }
    }
}