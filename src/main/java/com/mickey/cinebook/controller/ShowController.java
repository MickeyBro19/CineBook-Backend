package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.ShowRequest;
import com.mickey.cinebook.dto.ShowResponse;
import com.mickey.cinebook.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/show")
@RequiredArgsConstructor
public class ShowController {
	
	private final ShowService showService;
	
	@GetMapping
	public ResponseEntity<List<ShowResponse>> findAll() {
		
		return ResponseEntity.ok(showService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ShowResponse> findById(
			@PathVariable Long id) {
		
		return ResponseEntity.ok(showService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<ShowResponse> create(
			@Valid @RequestBody ShowRequest request) {
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(showService.create(request));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ShowResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody ShowRequest request) {
		
		return ResponseEntity.ok(
				showService.update(id, request)
		                        );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@PathVariable Long id) {
		
		showService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}