package com.internship.contractmanagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * Handles everything JWT-related: creating a token at login time, and
 * reading/validating a token on every subsequent request.
 *
 * Think of a JWT as a signed, tamper-proof note: "this user is Priya
 * (id=1), her roles are [EDITOR], and this note expires at 6pm tomorrow."
 * Anyone can READ it, but only our server (which knows the secret key)
 * could have SIGNED it - so if anyone tampers with it, validation fails.
 */
@Component
public class JwtUtil {

    private final Key signingKey;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration-ms}") long expirationMs) {
        // Turns our plain-text secret string into a proper cryptographic key
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    // ---------- Create a token at login time ----------
    public String generateToken(Long userId, String email, List<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(email)              // "who this token belongs to"
                .claim("userId", userId)        // extra custom data we stash inside
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256) // the tamper-proof signature
                .compact();                     // turns it into the final token string
    }

    // ---------- Read data back out of a token ----------
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class)).longValue();
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }

    // ---------- Check a token is still valid ----------
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true; // if parsing didn't throw, the signature and expiry are both fine
        } catch (Exception e) {
            return false; // tampered, malformed, or expired token
        }
    }
}