package com.toro.backend.infrastructure.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.security.properties.AppJwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final AppJwtProperties properties;
    private final SecretKey secretKey;

    public JwtService(AppJwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }



    public String generateAccessToken(User user, Instant now, Instant expiresAt) {
        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .claim("roles", List.of("")) // EMPTY ROLES
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user, String jti, Instant now, Instant expiresAt) {
        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(user.getId().toString())
                .id(jti)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return parser(token).getPayload();
    }

    public Long extractAccountId(String token) {
        Claims claims = parseClaims(token);
        Object accountIdObject = claims.get("accountId");
        if (accountIdObject != null) {
            try {
                return Long.parseLong(accountIdObject.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    // public String extractUsername(String token) {
    //     return parseClaims(token).getSubject();
    // }

    public List<String> extractRoles(String token) {
        Claims claims = parseClaims(token);
        Object rolesObject = claims.get("roles");
        if (rolesObject instanceof List<?> rolesList) {
            return rolesList.stream().map(String::valueOf).toList();
        }
        return Collections.emptyList();
    }

    public String extractJti(String token) {
        return parseClaims(token).getId();
    }

    public Instant extractExpiration(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration != null ? expiration.toInstant() : null;
    }

    public String generateJti() {
        return UUID.randomUUID().toString();
    }


    // PRIVATE METHODS
    private Jws<Claims> parser(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(properties.issuer())
                .build()
                .parseSignedClaims(token);
    }

}
