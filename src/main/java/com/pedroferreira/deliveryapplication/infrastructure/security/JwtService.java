package com.pedroferreira.deliveryapplication.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * ⚠️ VERSÃO LEGACY para JJWT 0.9.x
 * RECOMENDADO: Atualize para JJWT 0.11.5+ ou 0.12.6
 */
@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret:YXBsaWNhY2FvZGVsaXZlcnlzZWNyZXRrZXkyMDI0c3ByaW5nYm9vdGp3dHRva2Vu}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username, String role) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);
        return generateToken(extraClaims, username);
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {
        return buildToken(extraClaims, username, jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String username, long expiration) {
        long currentTimeMillis = System.currentTimeMillis();

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + expiration))
                .signWith(getSignInKey()) // ✅ LEGACY: signWith aceita 2 parâmetros
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        String username = null;

        try {
            username = extractUsername(token);

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);

        } catch (ExpiredJwtException e) {
            log.warn("Token expired for user: {}", username);
            return false;

        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
            return false;

        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return false;

        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            return false;

        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return false;

        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * ✅ VERSÃO LEGACY - Usa parser() ao invés de parserBuilder()
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    /**
     * ✅ VERSÃO LEGACY - Cria SecretKeySpec manualmente
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    public long getTimeUntilExpiration(String token) {
        Date expiration = extractExpiration(token);
        return expiration.getTime() - System.currentTimeMillis();
    }

    public boolean isTokenExpiringSoon(String token, long minutesThreshold) {
        long timeRemaining = getTimeUntilExpiration(token);
        long thresholdMillis = minutesThreshold * 60 * 1000;
        return timeRemaining > 0 && timeRemaining < thresholdMillis;
    }
}