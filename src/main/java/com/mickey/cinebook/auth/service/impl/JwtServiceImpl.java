package com.mickey.cinebook.auth.service.impl;

import com.mickey.cinebook.auth.service.JwtService;
import com.mickey.cinebook.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
	private final SecretKey secretKey;
	private final Long accessExpTtl;
	private final Long refreshExpTtl;
	
	public JwtServiceImpl(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.accessTokenExpiration}") Long accessExpTtl, @Value("${jwt.refreshTokenExpiration}") Long refreshExpTtl) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessExpTtl = accessExpTtl;
		this.refreshExpTtl = refreshExpTtl;
	}
	
	public String generateToken(User user) {
		Instant now = Instant.now();
		return Jwts.builder()
				.subject(user.getId().toString())
				.claim("email", user.getEmail())
				.claim("role", user.getRole().name())
				.claim("typ", "access")
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusSeconds(accessExpTtl)))
				.signWith(secretKey)
				.compact();
	}
	
	public String generateRefreshToken(User user, String jti) {
		Instant now = Instant.now();
		return Jwts
				.builder()
				.id(jti)
				.subject(user.getId().toString())
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusSeconds(refreshExpTtl)))
				.claim("typ", "refresh")
				.signWith(secretKey)
				.compact();
	}
	
	public boolean isAccessToken(String token) {
		Claims claims = parse(token);
		return "access".equals(claims.get("typ"));
	}
	
	public Claims parse(String token) {
		try {
			return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
		}
		catch (Exception e) {
			throw new JwtException("Invalid JWT token");
		}
	}
	
	public boolean isRefreshToken(String token) {
		Claims claims = parse(token);
		return "refresh".equals(claims.get("typ"));
	}
	
	public Long getUserId(String token) {
		Claims claims = parse(token);
		return Long.valueOf(claims.getSubject());
	}
	
	public Long accessTokenExpiration(String token) {
		Claims claims = parse(token);
		return claims.getExpiration().getTime();
	}
	
	@Override
	public Long getAccessTokenTtl() {
		return accessExpTtl;
	}
	
	@Override
	public Long getRefreshTokenTtl() {
		return refreshExpTtl;
	}
	
	@Override
	public Long refreshTokenExpiration(String token) {
		Claims claims = parse(token);
		return claims.getExpiration().getTime();
	}
	
	@Override
	public String getJti(String refreshToken) {
		return parse(refreshToken).getId();
	}
}
