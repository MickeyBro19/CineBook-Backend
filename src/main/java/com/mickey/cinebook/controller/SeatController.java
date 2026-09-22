package com.mickey.cinebook.controller;

import com.mickey.cinebook.dto.SeatRequest;
import com.mickey.cinebook.dto.SeatResponse;
import com.mickey.cinebook.service.SeatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
		name = "Seats",
		description = "Seat management APIs"
)
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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SeatResponse> create(
			@Valid @RequestBody SeatRequest request
	                                          ) {
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(seatService.create(request));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SeatResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody SeatRequest request
	                                          ) {
		
		return ResponseEntity.ok(
				seatService.update(id, request)
		                        );
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(
			@PathVariable Long id
	                                  ) {
		
		seatService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}