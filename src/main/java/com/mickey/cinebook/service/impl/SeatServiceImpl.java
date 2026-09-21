package com.mickey.cinebook.service.impl;

import com.mickey.cinebook.dto.SeatRequest;
import com.mickey.cinebook.dto.SeatResponse;
import com.mickey.cinebook.entity.Screen;
import com.mickey.cinebook.entity.Seat;
import com.mickey.cinebook.exception.ResourceNotFoundException;
import com.mickey.cinebook.repository.ScreenRepository;
import com.mickey.cinebook.repository.SeatRepository;
import com.mickey.cinebook.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatServiceImpl implements SeatService {
	
	private final SeatRepository seatRepository;
	private final ScreenRepository screenRepository;
	
	private SeatResponse toResponse(Seat seat) {
		
		return new SeatResponse(
				seat.getId(),
				seat.getSeatNumber(),
				seat.getRowLabel(),
				seat.getSeatType(),
				seat.getScreen().getId(),
				seat.getScreen().getName(),
				seat.isActive(),
				seat.getCreatedAt(),
				seat.getUpdatedAt()
		);
	}
	
	@Override
	public List<SeatResponse> findAll() {
		
		return seatRepository.findByIsActiveTrue()
				.stream()
				.map(this::toResponse)
				.toList();
	}
	
	@Override
	public List<SeatResponse> findByScreen(Long screenId) {
		
		screenRepository.findByIdAndIsActive(screenId, true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Screen not found"));
		
		return seatRepository.findByScreenIdAndIsActiveTrue(screenId)
				.stream()
				.map(this::toResponse)
				.toList();
	}
	
	@Override
	public SeatResponse findById(Long id) {
		
		Seat seat = seatRepository
				.findByIdAndIsActive(id, true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Seat not found"));
		
		return toResponse(seat);
	}
	
	@Override
	public SeatResponse create(SeatRequest request) {
		
		Screen screen = screenRepository
				.findByIdAndIsActive(request.screenId(), true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Screen not found"));
		
		String seatNumber = request.seatNumber().trim().toUpperCase();
		
		if (seatRepository.existsByScreenIdAndSeatNumberIgnoreCase(
				request.screenId(),
				seatNumber
		                                                          )) {
			throw new IllegalArgumentException(
					"Seat already exists in this screen"
			);
		}
		
		Seat seat = Seat.builder()
				.seatNumber(seatNumber)
				.rowLabel(request.rowLabel().trim().toUpperCase())
				.seatType(request.seatType())
				.screen(screen)
				.build();
		
		return toResponse(seatRepository.save(seat));
	}
	
	@Override
	public SeatResponse update(Long id, SeatRequest request) {
		
		Seat seat = seatRepository
				.findByIdAndIsActive(id, true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Seat not found"));
		
		Screen screen = screenRepository
				.findByIdAndIsActive(request.screenId(), true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Screen not found"));
		
		String seatNumber = request.seatNumber()
				.trim()
				.toUpperCase();
		
		boolean duplicate =
				seatRepository.existsByScreenIdAndSeatNumberIgnoreCaseAndIdNot(
						request.screenId(),
						seatNumber,
						id
				                                                              );
		
		if (duplicate) {
			throw new IllegalArgumentException(
					"Seat already exists in this screen"
			);
		}
		
		seat.setSeatNumber(seatNumber);
		seat.setRowLabel(request.rowLabel().trim().toUpperCase());
		seat.setSeatType(request.seatType());
		seat.setScreen(screen);
		
		return toResponse(seatRepository.save(seat));
	}
	
	@Override
	public void delete(Long id) {
		
		Seat seat = seatRepository
				.findByIdAndIsActive(id, true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Seat not found"));
		
		seat.setActive(false);
		
		seatRepository.save(seat);
	}
}