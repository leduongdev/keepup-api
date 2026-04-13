package com.ra.base_spring_boot.config.security.jwt;


import com.ra.base_spring_boot.config.security.principle.AccountPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expired.access}")
    private long expirationMs;

    @Value("${jwt.expired.refresh}")
    private long refreshMs;

    // Chuyển chuỗi Secret thành Key chuẩn để ký
    private Key key() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 1. Hàm tạo Access Token (Dùng expirationMs)
    public String generateAccessToken(AccountPrincipal principal) {
        return generateToken(principal, expirationMs);
    }

    // 2. Hàm tạo Refresh Token (Dùng refreshMs)
    public String generateRefreshToken(AccountPrincipal principal) {
        return generateToken(principal, refreshMs);
    }

    // Hàm tạo Token dùng chung (Nhận vào thời gian hết hạn)
    private String generateToken(AccountPrincipal principal, long expiryTime) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiryTime);

        return Jwts.builder()
                .setSubject(principal.getEmail())
                .claim("id", principal.getId())
                .claim("roles", principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Token has expired: " + e.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            System.err.println("Token error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Invalid token: " + e.getMessage());
        }
        return false;
    }

    // Lấy ngày hết hạn để lưu vào BlacklistToken nếu cần
    public Date getExpiryDateFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public long getRemainingTime(String token) {
        Date expiration = getExpiryDateFromToken(token);
        long remainingTime = expiration.getTime() - System.currentTimeMillis();
        return Math.max(0, remainingTime);
    }
}
