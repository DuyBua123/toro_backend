package com.toro.backend.infrastructure.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HexFormat;
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
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Base64.Encoder BASE64_URL = Base64.getUrlEncoder().withoutPadding();


    public JwtService(AppJwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }



    public Instant generateAccessTokenExpiresAt() {
        return Instant.now()
                .plus(Duration.ofMinutes(properties.accessTokenTtlMinutes()))
                .truncatedTo(ChronoUnit.SECONDS);
    }

    public Instant generateRefreshTokenExpiresAt() {
        return Instant.now()
                .plus(Duration.ofDays(properties.refreshTokenTtlDays()))
                .truncatedTo(ChronoUnit.SECONDS);
    }

    public String generateAccessToken(User user, Instant issueAt, Instant expiresAt) {
        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(user.getId().toString())
                .issuedAt(Date.from(issueAt))
                .expiration(Date.from(expiresAt))
                .claim("roles", List.of("")) // EMPTY ROLES
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken() {

        byte[] bytes = new byte[32]; // 256 bits
        SECURE_RANDOM.nextBytes(bytes);

        return BASE64_URL.encodeToString(bytes);
        // return Jwts.builder()
        //         .issuer(properties.issuer())
        //         .subject(user.getId().toString())
        //         .id(jti)
        //         .issuedAt(Date.from(issueAt))
        //         .expiration(Date.from(expiresAt))
        //         .signWith(secretKey)
        //         .compact();
    }

    public String hashRefreshToken(String refreshToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                refreshToken.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
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
