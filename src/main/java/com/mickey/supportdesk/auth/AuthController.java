package com.mickey.supportdesk.auth;

import com.mickey.supportdesk.dto.UserRequestDto;
import com.mickey.supportdesk.dto.UserResponseDto;
import com.mickey.supportdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
		userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
		return ResponseEntity.ok(userService.registerUser(userRequestDto));

	}
}
