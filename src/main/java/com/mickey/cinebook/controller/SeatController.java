package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.SeatRequest;
import com.mickey.cinebook.dto.SeatResponse;
import com.mickey.cinebook.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
	
	private final SeatService seatService;
	
	@GetMapping
	public ResponseEntity<List<SeatResponse>> findAll() {
		
		return ResponseEntity.ok(
				seatService.findAll()
		                        );
	}
	
	@GetMapping("/screen/{screenId}")
	public ResponseEntity<List<SeatResponse>> findByScreen(
			@PathVariable Long screenId
	                                                      ) {
		
		return ResponseEntity.ok(
				seatService.findByScreen(screenId)
		                        );
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SeatResponse> findById(
			@PathVariable Long id
	                                            ) {
		
		return ResponseEntity.ok(
				seatService.findById(id)
		                        );
	}
	
	@PostMapping
	public ResponseEntity<SeatResponse> create(
			@Valid @RequestBody SeatRequest request
	                                          ) {
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(seatService.create(request));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SeatResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody SeatRequest request
	                                          ) {
		
		return ResponseEntity.ok(
				seatService.update(id, request)
		                        );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@PathVariable Long id
	                                  ) {
		
		seatService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}