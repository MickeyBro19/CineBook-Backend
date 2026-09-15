package com.mickey.supportdesk.auth.controller;

import com.mickey.supportdesk.auth.service.JwtService;
import com.mickey.supportdesk.auth.service.UserService;
import com.mickey.supportdesk.dto.LoginRequest;
import com.mickey.supportdesk.dto.TokenResponse;
import com.mickey.supportdesk.dto.UserRequestDto;
import com.mickey.supportdesk.dto.UserResponseDto;
import com.mickey.supportdesk.entity.User;
import com.mickey.supportdesk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final ModelMapper modelMapper;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
		userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
		return ResponseEntity.ok(userService.registerUser(userRequestDto));
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = getAuthentication(loginRequest);
		User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
		if (!user.isEnabled()) {
			throw new DisabledException("User is disabled");
		}
		String accessToken = jwtService.generateToken(user);
		TokenResponse tokenResponse = TokenResponse.of(
				accessToken,
				"",
				jwtService.accessTokenExpiration(accessToken),
				jwtService.isAccessToken(accessToken) ? "Access Token" : "Refresh Token",
				modelMapper.map(user, UserResponseDto.class)
		                                              );
		return ResponseEntity.ok(tokenResponse);
	}
	
	private Authentication getAuthentication(LoginRequest loginRequest) {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
	}
}
