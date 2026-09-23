package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.ScreenRequest;
import com.mickey.cinebook.dto.ScreenResponse;
import com.mickey.cinebook.service.ScreenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Tag(
		name = "Screens",
		description = "Cinema screen management APIs"
)
@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {
	
	private final ScreenService screenService;
	
	@GetMapping
	public ResponseEntity<Page<ScreenResponse>> findAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return ResponseEntity.ok(screenService.findAll(pageable));
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