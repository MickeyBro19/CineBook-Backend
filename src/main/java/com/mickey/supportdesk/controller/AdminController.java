package com.mickey.supportdesk.controller;

import com.mickey.supportdesk.auth.service.UserService;
import com.mickey.supportdesk.auth.service.impl.UserServiceImpl;
import com.mickey.supportdesk.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		return ResponseEntity.ok(userService.findAllUsers());
	}
}
