package com.mickey.cinebook.service.impl;

import com.mickey.cinebook.dto.TheatreRequest;
import com.mickey.cinebook.dto.TheatreResponse;
import com.mickey.cinebook.entity.Theatre;
import com.mickey.cinebook.exception.ResourceNotFoundException;
import com.mickey.cinebook.repository.TheatreRepository;
import com.mickey.cinebook.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {
	
	private final TheatreRepository theatreRepository;
	
	@Override
	public List<TheatreResponse> findAll() {
		return theatreRepository.findByIsActiveTrue()
				.stream()
				.map(this :: toResponse)
				.toList();
	}
	
	private TheatreResponse toResponse(Theatre theatre) {
		return new TheatreResponse(
				theatre.getId(),
				theatre.getName(),
				theatre.getAddress(),
				theatre.getCity(),
				theatre.isActive(),
				theatre.getCreatedAt(),
				theatre.getUpdatedAt()
		);
	}
	
	@Override
	public TheatreResponse findById(Long id) {
		return theatreRepository.findByIdAndIsActiveTrue(id)
				.map(this :: toResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Theatre doesn't exist"));
	}
	
	@Override
	public TheatreResponse create(TheatreRequest request) {
		
		String name = request.name().trim();
		String city = request.city().trim().toUpperCase();
		
		if (theatreRepository.existsByNameIgnoreCaseAndCityIgnoreCase(name, city)) {
			throw new IllegalArgumentException("Theatre already exists");
		}
		
		Theatre theatre = Theatre.builder()
				.name(name)
				.address(request.address().trim())
				.city(city)
				.build();
		
		return toResponse(theatreRepository.save(theatre));
	}
	
	@Override
	public TheatreResponse update(Long id, TheatreRequest request) {
		
		Theatre theatre = theatreRepository.findByIdAndIsActiveTrue(id)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Theatre doesn't exist"));
		
		String name = request.name().trim();
		String city = request.city().trim().toUpperCase();
		
		if (theatreRepository.existsByNameIgnoreCaseAndCityIgnoreCaseAndIdNot(
				name, city, id)) {
			throw new IllegalArgumentException("Theatre already exists");
		}
		
		theatre.setName(name);
		theatre.setAddress(request.address().trim());
		theatre.setCity(city);
		
		return toResponse(theatreRepository.save(theatre));
	}
	
	@Override
	public void delete(Long id) {
		
		Theatre theatre = theatreRepository.findByIdAndIsActiveTrue(id)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Theatre doesn't exist"));
		
		theatre.setActive(false);
		theatreRepository.save(theatre);
	}
}