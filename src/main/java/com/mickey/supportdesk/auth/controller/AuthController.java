package com.mickey.supportdesk.auth.controller;

import com.mickey.supportdesk.auth.service.JwtService;
import com.mickey.supportdesk.auth.service.UserService;
import com.mickey.supportdesk.dto.LoginRequest;
import com.mickey.supportdesk.dto.TokenResponse;
import com.mickey.supportdesk.dto.UserRequestDto;
import com.mickey.supportdesk.dto.UserResponseDto;
import com.mickey.supportdesk.entity.RefreshToken;
import com.mickey.supportdesk.entity.User;
import com.mickey.supportdesk.repository.RefreshTokenRepository;
import com.mickey.supportdesk.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
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
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
		return ResponseEntity.ok(userService.registerUser(userRequestDto));
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
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
		TokenResponse tokenResponse = TokenResponse.of(
				accessToken,
				refreshToken,
				jwtService.accessTokenExpiration(accessToken),
				jwtService.isAccessToken(accessToken) ? "Access Token" : "Refresh Token",
				modelMapper.map(user, UserResponseDto.class));
		return ResponseEntity.ok(tokenResponse);
	}

}
