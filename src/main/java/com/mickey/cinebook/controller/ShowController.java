package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.ShowRequest;
import com.mickey.cinebook.dto.ShowResponse;
import com.mickey.cinebook.service.ShowService;
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

import java.util.List;


@Tag(
		name = "Shows",
		description = "Movie show scheduling APIs"
)
@RestController
@RequestMapping("/api/show")
@RequiredArgsConstructor
public class ShowController {
	
	private final ShowService showService;
	
	@GetMapping
	public ResponseEntity<Page<ShowResponse>> findAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		
		return ResponseEntity.ok(showService.findAll(pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ShowResponse> findById(
			@PathVariable Long id) {
		
		return ResponseEntity.ok(showService.findById(id));
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ShowResponse> create(
			@Valid @RequestBody ShowRequest request) {
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(showService.create(request));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ShowResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody ShowRequest request) {
		
		return ResponseEntity.ok(
				showService.update(id, request)
		                        );
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(
			@PathVariable Long id) {
		
		showService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}