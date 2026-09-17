package com.mickey.cinebook.auth.controller;

import com.mickey.cinebook.auth.dto.*;
import com.mickey.cinebook.auth.service.CookieService;
import com.mickey.cinebook.auth.service.JwtService;
import com.mickey.cinebook.auth.service.UserService;
import com.mickey.cinebook.auth.entity.RefreshToken;
import com.mickey.cinebook.auth.entity.User;
import com.mickey.cinebook.auth.repository.RefreshTokenRepository;
import com.mickey.cinebook.auth.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final ModelMapper modelMapper;
	private final RefreshTokenRepository refreshTokenRepository;
	private final CookieService cookieService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
		return ResponseEntity.ok(userService.registerUser(userRequestDto));
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email().toLowerCase().trim(), loginRequest.password()));
		User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
		if (!user.isEnabled()) {
			throw new DisabledException("User is disabled");
		}
		String accessToken = jwtService.generateToken(user);
		String jti = UUID.randomUUID().toString();
		RefreshToken refreshTokenOb = RefreshToken.builder()
				.jti(jti)
				.user(user)
				.createdAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTokenTtl()))
				.revoked(false)
				.build();
		refreshTokenRepository.save(refreshTokenOb);
		String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());
		
		cookieService.attachRefreshTokenCookie(response, refreshToken, jwtService.getRefreshTokenTtl());
		cookieService.addNoStoreHeaders(response);
		
		TokenResponseDto tokenResponse = TokenResponseDto.of(
				accessToken,
				refreshToken,
				jwtService.accessTokenExpiration(accessToken),
				jwtService.isAccessToken(accessToken) ? "Access Token" : "Refresh Token",
				modelMapper.map(user, UserResponseDto.class));
		return ResponseEntity.ok(tokenResponse);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<TokenResponseDto> refreshToken(
			HttpServletResponse response,
			HttpServletRequest request,
			@RequestBody(required = false) RefreshTokenRequest body
	                                                    ) {
		String refreshToken = readRefreshTokenFromBodyOrRequest(body, request).orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
		if (!jwtService.isRefreshToken(refreshToken)) {
			throw new BadCredentialsException("Invalid refresh token");
		}
		String jti = jwtService.getJti(refreshToken);
		Long userId = jwtService.getUserId(refreshToken);
		RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti).orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
		if (storedRefreshToken.getExpiresAt().isBefore(Instant.now())) {
			throw new BadCredentialsException("Invalid refresh token");
		}
		if (storedRefreshToken.isRevoked()) {
			throw new BadCredentialsException("Invalid refresh token");
		}
		if (!storedRefreshToken.getUser().getId().equals(userId)) {
			throw new BadCredentialsException("Invalid refresh token");
		}
		
		storedRefreshToken.setRevoked(true);
		refreshTokenRepository.save(storedRefreshToken);
		
		String newJti = UUID.randomUUID().toString();
		User user = storedRefreshToken.getUser();
		
		RefreshToken newRefreshTokenOb = RefreshToken.builder()
				.jti(newJti)
				.user(user)
				.createdAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTokenTtl()))
				.revoked(false)
				.build();
		refreshTokenRepository.save(newRefreshTokenOb);
		
		String newAccessToken = jwtService.generateToken(user);
		String newRefreshToken = jwtService.generateRefreshToken(user, newJti);
		
		cookieService.attachRefreshTokenCookie(response, newRefreshToken, jwtService.getRefreshTokenTtl());
		cookieService.addNoStoreHeaders(response);
		
		return ResponseEntity.ok(TokenResponseDto.of(
				newAccessToken,
				newRefreshToken,
				jwtService.getRefreshTokenTtl(),
				"refresh Token",
				modelMapper.map(user, UserResponseDto.class)));
	}
	
	private Optional<String> readRefreshTokenFromBodyOrRequest(RefreshTokenRequest body, HttpServletRequest request) {
		//prefer reading from cookie
		if (request.getCookies() != null) {
			Optional<String> fromCookie = Arrays.stream(request.getCookies())
					.filter(c -> cookieService.getRefreshTokenCookieName().equals(c.getName()))
					.map(Cookie :: getValue)
					.filter(v -> !v.isBlank())
					.findFirst();
			if (fromCookie.isPresent()) {
				return fromCookie;
			}
		}
		
		//from body
		if (body != null && body.RefreshToken() != null && !body.RefreshToken().isBlank()) {
			return Optional.of(body.RefreshToken());
		}
		
		//custom header
		String refreshHeader = request.getHeader("X-Refresh-Token");
		if (refreshHeader != null && !refreshHeader.isBlank()) {
			return Optional.of(refreshHeader);
		}
		
		//Authorization=Bearer<Token>
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.regionMatches(true, 0, "Bearer", 7, authHeader.length())) {
			String candidate = authHeader.substring(7).trim();
			if (!candidate.isEmpty()) {
				try {
					if (jwtService.isRefreshToken(candidate)) {
						return Optional.of(candidate);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					throw new BadCredentialsException("Invalid Refresh Token");
				}
			}
		}
		
		//else
		return Optional.empty();
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		readRefreshTokenFromBodyOrRequest(null, request).ifPresent(token -> {
			try {
				if (jwtService.isRefreshToken(token)) {
					String jti = jwtService.getJti(token);
					refreshTokenRepository.findByJti(jti).ifPresent(refreshToken -> {
						refreshToken.setRevoked(true);
						refreshTokenRepository.save(refreshToken);
					});
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new BadCredentialsException("Invalid Refresh Token");
			}
		});
		cookieService.clearRefreshTokenCookie(response);
		cookieService.addNoStoreHeaders(response);
		SecurityContextHolder.clearContext();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
