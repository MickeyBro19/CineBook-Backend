package com.mickey.cinebook.auth.service;

import com.mickey.cinebook.auth.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {
	
	public String generateToken(User user);
	
	String generateRefreshToken(User user, String jti);
	
	boolean isAccessToken(String token);
	
	Claims parse(String token);
	
	boolean isRefreshToken(String token);
	
	Long getUserId(String token);
	
	Long accessTokenExpiration(String token);
	
	Long getAccessTokenTtl();
	
	Long getRefreshTokenTtl();
	
	Long refreshTokenExpiration(String token);
	
	String getJti(String refreshToken);
}
