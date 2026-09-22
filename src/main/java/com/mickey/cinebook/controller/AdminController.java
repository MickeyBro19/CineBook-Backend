package com.mickey.cinebook.controller;

import com.mickey.cinebook.auth.service.impl.UserServiceImpl;
import com.mickey.cinebook.auth.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
	private final UserServiceImpl userService;
	
	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		return ResponseEntity.ok(userService.findAllUsers());
	}
}
