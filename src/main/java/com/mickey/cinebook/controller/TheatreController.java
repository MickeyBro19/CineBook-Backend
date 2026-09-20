package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.TheatreRequest;
import com.mickey.cinebook.dto.TheatreResponse;
import com.mickey.cinebook.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<TheatreResponse> create(@Valid @RequestBody TheatreRequest theatreRequest) {
		return ResponseEntity.ok(theatreService.create(theatreRequest));
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<TheatreResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(theatreService.findById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TheatreResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody TheatreRequest theatreRequest) {
		return ResponseEntity.ok(theatreService.update(id, theatreRequest));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		theatreService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
