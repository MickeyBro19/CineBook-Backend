package com.mickey.cinebook.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
	private final String jwtRefreshTokenCookieName;
	private final boolean cookieSecure;
	private final boolean cookieHttpOnly;
	private final String cookieDomain;
	private final String cookieSameSite;
	
	public CookieService(
			@Value("${jwt.refresh-token-cookie-name}") String jwtRefreshTokenCookieName,
			@Value("${jwt.cookie-secure}") boolean cookieSecure,
			@Value("${jwt.cookie-http-only}") boolean cookieHttpOnly,
			@Value("${jwt.cookie-domain}") String cookieDomain,
			@Value("${jwt.cookie-same-site}") String cookieSameSite
	                    ) {
		this.jwtRefreshTokenCookieName = jwtRefreshTokenCookieName;
		this.cookieSecure = cookieSecure;
		this.cookieHttpOnly = cookieHttpOnly;
		this.cookieDomain = cookieDomain;
		this.cookieSameSite = cookieSameSite;
	}
	
	public void attachRefreshTokenCookie(HttpServletResponse response, String token, Long maxAge) {
		var responseCookieBuilder = ResponseCookie.from(jwtRefreshTokenCookieName, token)
				.httpOnly(cookieHttpOnly)
				.path("/")
				.maxAge(maxAge)
				.sameSite(cookieSameSite);
		if (cookieDomain != null && !cookieDomain.isBlank()) {
			responseCookieBuilder.domain(cookieDomain);
		}
		ResponseCookie cookie = responseCookieBuilder.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public void clearRefreshTokenCookie(HttpServletResponse response) {
		var responseCookieBuilder = ResponseCookie.from(jwtRefreshTokenCookieName, "")
				.httpOnly(cookieHttpOnly)
				.path("/")
				.sameSite(cookieSameSite)
				.maxAge(0)
				.secure(cookieSecure);
		if (cookieDomain != null && !cookieDomain.isBlank()) {
			responseCookieBuilder.domain(cookieDomain);
		}
		ResponseCookie cookie = responseCookieBuilder.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public void addNoStoreHeaders(HttpServletResponse response) {
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
		response.setHeader(HttpHeaders.PRAGMA, "no-cache");
	}
	
	public String getRefreshTokenCookieName() {
		return jwtRefreshTokenCookieName;
	}
}
