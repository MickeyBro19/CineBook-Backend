package com.mickey.cinebook.service.impl;

import com.mickey.cinebook.dto.MovieRequest;
import com.mickey.cinebook.dto.MovieResponse;
import com.mickey.cinebook.entity.Movie;
import com.mickey.cinebook.exception.ResourceNotFoundException;
import com.mickey.cinebook.repository.MovieRepository;
import com.mickey.cinebook.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
	private final MovieRepository movieRepository;
	
	@Override
	public Page<MovieResponse> findAll(Pageable pageable) {
		Page<Movie> moviePage = movieRepository.findAll(pageable);
		return moviePage.map(this::toResponse);
	}
	
	private MovieResponse toResponse(Movie movie) {
		return new MovieResponse(
				movie.getId(),
				movie.getTitle(),
				movie.getDescription(),
				movie.getGenre(),
				movie.isActive(),
				movie.getCreatedAt(),
				movie.getUpdatedAt()
		);
	}
	
	@Override
	public MovieResponse findById(Long id) {
		Movie movie = movieRepository.findById(id)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Movie doesn't exist"));
		
		if (!movie.isActive()) {
			throw new ResourceNotFoundException("Movie doesn't exist");
		}
		
		return toResponse(movie);	}
	
	
	@Override
	public MovieResponse create(MovieRequest movieRequest) {
		if (movieRepository.existsByTitleIgnoreCase(movieRequest.title())) {
			throw new IllegalArgumentException("Movie already exists");
		}
		Movie movie = Movie.builder()
				.title(movieRequest.title().trim()).description(movieRequest.description()).genre(movieRequest.genre()).build();
		return toResponse(movieRepository.save(movie));
	}
	
	
	@Override
	public MovieResponse update(Long id, MovieRequest movieRequest) {
		
		Movie movie = movieRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
		
		movie.setTitle(movieRequest.title().trim());
		movie.setDescription(movieRequest.description());
		movie.setGenre(movieRequest.genre());
		
		return toResponse(movieRepository.save(movie));
	}
	
	@Override
	public void delete(Long id) {
		
		Movie movie = movieRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
		
		movie.setActive(false);
		movieRepository.save(movie);
	}
	
	@Override
	public MovieResponse findByName(String name) {
		Movie movie = movieRepository.findByTitleIgnoreCase(name)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Movie doesn't exist"));
		
		if (!movie.isActive()) {
			throw new ResourceNotFoundException("Movie doesn't exist");
		}
		
		return toResponse(movie);
	}
}
