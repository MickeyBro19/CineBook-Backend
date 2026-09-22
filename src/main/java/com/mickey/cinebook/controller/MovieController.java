package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.MovieRequest;
import com.mickey.cinebook.dto.MovieResponse;
import com.mickey.cinebook.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {
	
	private final MovieService movieService;
	
	@GetMapping
	public ResponseEntity<List<MovieResponse>> findAll() {
		return ResponseEntity.ok(movieService.findAll());
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest movieRequest) {
		return ResponseEntity.ok(movieService.create(movieRequest));
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<MovieResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(movieService.findById(id));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MovieResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody MovieRequest movieRequest) {
		return ResponseEntity.ok(movieService.update(id, movieRequest));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		movieService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
