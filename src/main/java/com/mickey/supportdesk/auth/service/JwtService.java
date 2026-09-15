package com.mickey.supportdesk.auth.service;

import com.mickey.supportdesk.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {
	
	public String generateToken(User user);
	
	public String generateRefreshToken(User user, String jti);
	
	public boolean isAccessToken(String token);
	
	public Claims parse(String token);
	
	public boolean isRefreshToken(String token);
	
	public Long getUserId(String token);
	
	public Long accessTokenExpiration(String token);
}
