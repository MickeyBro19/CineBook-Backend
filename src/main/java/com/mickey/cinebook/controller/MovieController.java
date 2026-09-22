package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.MovieRequest;
import com.mickey.cinebook.dto.MovieResponse;
import com.mickey.cinebook.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
		name = "Movies",
		description = "Movie management APIs"
)
@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {
	
	private final MovieService movieService;
	
	@Operation(
			summary = "Get all active movies",
			description = "Returns all currently active movies"
	)
	@GetMapping
	public ResponseEntity<List<MovieResponse>> findAll() {
		return ResponseEntity.ok(movieService.findAll());
	}
	
	@Operation(
			summary = "Create a movie",
			description = "Creates a new movie. Admin access required."
	)
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest movieRequest) {
		return ResponseEntity.ok(movieService.create(movieRequest));
	}
	
	@Operation(
			summary = "Get movie by name",
			description = "Returns an active movie matching the supplied name"
	)
	@GetMapping("/{name}")
	public ResponseEntity<MovieResponse> findByName(
			@PathVariable String name) {
		
		return ResponseEntity.ok(movieService.findByName(name));
	}
//
//	@Operation(
//			summary = "Get movie by id",
//			description = "Returns an active movie matching the supplied id"
//	)
//	@GetMapping("/{id}")
//	public ResponseEntity<MovieResponse> findById(@PathVariable Long id) {
//		return ResponseEntity.ok(movieService.findById(id));
//	}
//
	@Operation(
			summary = "Update a movie",
			description = "Updates movie information. Admin access required."
	)
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MovieResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody MovieRequest movieRequest) {
		return ResponseEntity.ok(movieService.update(id, movieRequest));
	}
	@Operation(
			summary = "Deactivate a movie",
			description = "Soft deletes a movie. Admin access required."
	)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		movieService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
