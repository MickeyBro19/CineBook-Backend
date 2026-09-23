package com.mickey.cinebook.service.impl;

import com.mickey.cinebook.dto.ShowRequest;
import com.mickey.cinebook.dto.ShowResponse;
import com.mickey.cinebook.entity.Movie;
import com.mickey.cinebook.entity.Screen;
import com.mickey.cinebook.entity.Show;
import com.mickey.cinebook.exception.ResourceNotFoundException;
import com.mickey.cinebook.repository.MovieRepository;
import com.mickey.cinebook.repository.ScreenRepository;
import com.mickey.cinebook.repository.ShowRepository;
import com.mickey.cinebook.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {
	
	private final ShowRepository showRepository;
	private final MovieRepository movieRepository;
	private final ScreenRepository screenRepository;
	
	@Override
	public Page<ShowResponse> findAll(Pageable pageable) {
		Page<Show> shows = showRepository.findAll(pageable);
		
		return shows.map(this::toResponse);
	}
	
	private ShowResponse toResponse(Show show) {
		
		Screen screen = show.getScreen();
		
		return new ShowResponse(
				show.getId(),
				
				show.getMovie().getId(),
				show.getMovie().getTitle(),
				
				screen.getId(),
				screen.getName(),
				
				screen.getTheatre().getId(),
				screen.getTheatre().getName(),
				
				show.getStartTime(),
				show.getEndTime(),
				
				show.getTicketPrice(),
				
				show.isActive(),
				
				show.getCreatedAt(),
				show.getUpdatedAt()
		);
	}
	
	@Override
	public ShowResponse findById(Long id) {
		
		Show show = showRepository.findByIdAndIsActiveTrue(id)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Show not found"));
		
		return toResponse(show);
	}
	
	@Override
	public ShowResponse create(ShowRequest request) {
		
		validateTime(request);
		showContext context = checkShow(request);
		
		validateOverlap(
				request.screenId(),
				request.startTime(),
				request.endTime()
		               );
		
		Show show = Show.builder()
				.movie(context.movie)
				.screen(context.screen)
				.startTime(request.startTime())
				.endTime(request.endTime())
				.ticketPrice(request.ticketPrice())
				.build();
		
		return toResponse(showRepository.save(show));
	}
	
	@Override
	public ShowResponse update(Long id, ShowRequest request) {
		
		validateTime(request);
		
		Show show = showRepository.findByIdAndIsActiveTrue(id)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Show not found"));
		
		showContext context = checkShow(request);
		
		boolean overlapping = !showRepository
				.findOverlappingShowsForUpdate(
						request.screenId(),
						id,
						request.startTime(),
						request.endTime()
				                              )
				.isEmpty();
		
		if (overlapping) {
			throw new IllegalArgumentException(
					"Another show is already scheduled on this screen during this time"
			);
		}
		
		show.setMovie(context.movie);
		show.setScreen(context.screen);
		show.setStartTime(request.startTime());
		show.setEndTime(request.endTime());
		show.setTicketPrice(request.ticketPrice());
		
		return toResponse(showRepository.save(show));
	}
	
	@Override
	public void delete(Long id) {
		
		Show show = showRepository.findByIdAndIsActiveTrue(id)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Show not found"));
		
		show.setActive(false);
		
		showRepository.save(show);
	}
	
	private void validateTime(ShowRequest request) {
		
		if (!request.startTime().isBefore(request.endTime())) {
			throw new IllegalArgumentException(
					"Start time must be before end time"
			);
		}
	}
	
	private showContext checkShow(ShowRequest request) {
		Movie movie = movieRepository.findById(request.movieId())
				.orElseThrow(() ->
						             new ResourceNotFoundException("Movie not found"));
		
		Screen screen = screenRepository.findByIdAndIsActive(request.screenId(), true)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Screen not found"));
		
		if (!movie.isActive()) {
			throw new IllegalArgumentException("Movie is inactive");
		}
		
		if (!screen.isActive()) {
			throw new IllegalArgumentException("Screen is inactive");
		}
		return new showContext(movie, screen);
	}
	
	private void validateOverlap(
			Long screenId,
			java.time.LocalDateTime startTime,
			java.time.LocalDateTime endTime
	                            ) {
		
		boolean overlapping = !showRepository
				.findOverlappingShows(
						screenId,
						startTime,
						endTime
				                     )
				.isEmpty();
		
		if (overlapping) {
			throw new IllegalArgumentException(
					"Another show is already scheduled on this screen during this time"
			);
		}
	}
	
	private record showContext(Movie movie, Screen screen) {}
}