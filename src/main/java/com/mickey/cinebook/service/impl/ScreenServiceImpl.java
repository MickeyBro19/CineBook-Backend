package com.mickey.cinebook.service.impl;

import com.mickey.cinebook.dto.ScreenRequest;
import com.mickey.cinebook.dto.ScreenResponse;
import com.mickey.cinebook.entity.Screen;
import com.mickey.cinebook.entity.Theatre;
import com.mickey.cinebook.exception.ResourceNotFoundException;
import com.mickey.cinebook.repository.ScreenRepository;
import com.mickey.cinebook.repository.TheatreRepository;
import com.mickey.cinebook.service.ScreenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScreenServiceImpl implements ScreenService {
	
	private final ScreenRepository screenRepository;
	private final TheatreRepository theatreRepository;
	
	private ScreenResponse toResponse(Screen screen) {
		return new ScreenResponse(
				screen.getId(),
				screen.getName(),
				screen.getTheatre().getId(),
				screen.getTheatre().getName(),
				screen.isActive(),
				screen.getCreatedAt(),
				screen.getUpdatedAt()
		);
	}
	
	@Override
	public List<ScreenResponse> findAll() {
		return screenRepository.findByIsActiveTrue()
				.stream()
				.map(this::toResponse)
				.toList();
	}
	
	@Override
	public ScreenResponse findById(Long id) {
		Screen screen = screenRepository.findByIdAndIsActive(id, true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Screen not found"));
		
		return toResponse(screen);
	}
	
	@Override
	public ScreenResponse create(ScreenRequest request) {
		
		Theatre theatre = theatreRepository
				.findByIdAndIsActiveTrue(request.theatreId())
				.orElseThrow(() ->
						             new ResourceNotFoundException("Theatre not found"));
		
		if (screenRepository.existsByTheatreIdAndNameIgnoreCase(
				request.theatreId(),
				request.name().trim())) {
			
			throw new IllegalArgumentException("Screen already exists!");
		}
		
		Screen screen = Screen.builder()
				.name(request.name().trim())
				.theatre(theatre)
				.build();
		
		return toResponse(screenRepository.save(screen));
	}
	
	@Override
	public ScreenResponse update(Long id, ScreenRequest request) {
		
		Screen screen = screenRepository
				.findByIdAndIsActive(id, true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Screen not found"));
		
		Theatre theatre = theatreRepository
				.findByIdAndIsActiveTrue(request.theatreId())
				.orElseThrow(() ->
						             new ResourceNotFoundException("Theatre not found"));
		
		String newName = request.name().trim();
		
		boolean duplicate = screenRepository
				.existsByTheatreIdAndNameIgnoreCaseAndIdNot(
						request.theatreId(),
						newName,
						id
				                                           );
		
		if (duplicate) {
			throw new IllegalArgumentException(
					"A screen with this name already exists in this theatre"
			);
		}
		
		screen.setName(newName);
		screen.setTheatre(theatre);
		
		return toResponse(screenRepository.save(screen));
	}
	
	@Override
	public void delete(Long id) {
		
		Screen screen = screenRepository
				.findByIdAndIsActive(id, true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Screen not found"));
		
		screen.setActive(false);
		
		screenRepository.save(screen);
	}
}
