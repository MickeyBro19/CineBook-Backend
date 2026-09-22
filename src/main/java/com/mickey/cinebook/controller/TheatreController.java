package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.TheatreRequest;
import com.mickey.cinebook.dto.TheatreResponse;
import com.mickey.cinebook.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatre")
@RequiredArgsConstructor
public class TheatreController {
	
	private final TheatreService theatreService;
	
	@GetMapping
	public ResponseEntity<List<TheatreResponse>> findAll() {
		return ResponseEntity.ok(theatreService.findAll());
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<TheatreResponse> create(@Valid @RequestBody TheatreRequest theatreRequest) {
		return ResponseEntity.ok(theatreService.create(theatreRequest));
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<TheatreResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(theatreService.findById(id));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<TheatreResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody TheatreRequest theatreRequest) {
		return ResponseEntity.ok(theatreService.update(id, theatreRequest));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		theatreService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
