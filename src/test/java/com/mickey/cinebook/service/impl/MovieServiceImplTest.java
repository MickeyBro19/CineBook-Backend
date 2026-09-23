package com.mickey.cinebook.service.impl;

import com.mickey.cinebook.dto.MovieRequest;
import com.mickey.cinebook.dto.MovieResponse;
import com.mickey.cinebook.entity.Movie;
import com.mickey.cinebook.exception.ResourceNotFoundException;
import com.mickey.cinebook.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {
	
	@Mock
	private MovieRepository movieRepository;
	
	@InjectMocks
	private MovieServiceImpl movieService;
	
	private Movie movie;
	
	@BeforeEach
	void setUp() {
		
		movie = Movie.builder()
				.id(1L)
				.title("Interstellar")
				.description("A space exploration movie")
				.genre("Sci-Fi")
				.isActive(true)
				.build();
	}
	
	@Test
	void findByName_shouldReturnMovie() {
		
		when(movieRepository.findByTitleIgnoreCase("Interstellar"))
				.thenReturn(Optional.of(movie));
		
		MovieResponse response =
				movieService.findByName("Interstellar");
		
		assertNotNull(response);
		assertEquals("Interstellar", response.getTitle());
		assertEquals("Sci-Fi", response.getGenre());
		
		verify(movieRepository)
				.findByTitleIgnoreCase("Interstellar");
	}
	
	@Test
	void findByName_shouldThrowException_whenMovieDoesNotExist() {
		
		when(movieRepository.findByTitleIgnoreCase("Unknown"))
				.thenReturn(Optional.empty());
		
		assertThrows(
				ResourceNotFoundException.class,
				() -> movieService.findByName("Unknown")
		            );
		
		verify(movieRepository)
				.findByTitleIgnoreCase("Unknown");
	}
	
	@Test
	void create_shouldRejectDuplicateMovie() {
		
		MovieRequest request = new MovieRequest(
				"Interstellar",
				"A space exploration movie",
				"Sci-Fi"
		);
		
		when(movieRepository.existsByTitleIgnoreCase("Interstellar"))
				.thenReturn(true);
		
		assertThrows(
				IllegalArgumentException.class,
				() -> movieService.create(request)
		            );
		
		verify(movieRepository, never())
				.save(any(Movie.class));
	}
	
	@Test
	void create_shouldSaveMovie_whenMovieDoesNotExist() {
		
		MovieRequest request = new MovieRequest(
				"Inception",
				"A dream-based thriller",
				"Sci-Fi"
		);
		
		when(movieRepository.existsByTitleIgnoreCase("Inception"))
				.thenReturn(false);
		
		when(movieRepository.save(any(Movie.class)))
				.thenAnswer(invocation -> {
					
					Movie saved = invocation.getArgument(0);
					saved.setId(2L);
					
					return saved;
				});
		
		MovieResponse response =
				movieService.create(request);
		
		assertNotNull(response);
		assertEquals("Inception", response.getTitle());
		assertEquals("Sci-Fi", response.getGenre());
		
		verify(movieRepository)
				.existsByTitleIgnoreCase("Inception");
		
		verify(movieRepository)
				.save(any(Movie.class));
	}
}