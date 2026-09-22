package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.ScreenRequest;
import com.mickey.cinebook.dto.ScreenResponse;
import com.mickey.cinebook.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {
	
	private final ScreenService screenService;
	
	@GetMapping
	public ResponseEntity<List<ScreenResponse>> findAll() {
		return ResponseEntity.ok(screenService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ScreenResponse> findById(
			@PathVariable Long id
	                                              ) {
		return ResponseEntity.ok(screenService.findById(id));
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ScreenResponse> create(
			@Valid @RequestBody ScreenRequest request
	                                            ) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(screenService.create(request));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ScreenResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody ScreenRequest request
	                                            ) {
		return ResponseEntity.ok(
				screenService.update(id, request)
		                        );
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(
			@PathVariable Long id
	                                  ) {
		screenService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}