package com.mickey.cinebook.service.impl;

import com.mickey.cinebook.dto.ShowRequest;
import com.mickey.cinebook.dto.ShowResponse;
import com.mickey.cinebook.entity.Movie;
import com.mickey.cinebook.entity.Screen;
import com.mickey.cinebook.entity.Show;
import com.mickey.cinebook.entity.Theatre;
import com.mickey.cinebook.exception.ResourceNotFoundException;
import com.mickey.cinebook.repository.MovieRepository;
import com.mickey.cinebook.repository.ScreenRepository;
import com.mickey.cinebook.repository.ShowRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ShowServiceImpl Tests ")
class ShowServiceImplTest {
	@Mock
	private MovieRepository movieRepository;
	@Mock
	private ShowRepository showRepository;
	@Mock
	private ScreenRepository screenRepository;
	
	@InjectMocks
	private ShowServiceImpl showServiceImpl;
	
	private ShowRequest testShowRequest;
	private Movie movie;
	private Screen screen;
	private Theatre theatre;
	private Show show;
	
	
	@BeforeEach
	void setUp() {
		movie = Movie.builder()
				.id(1L).title("japan").description("A movie about exploring japan").genre("Documentary").build();
		theatre = Theatre.builder()
				.id(1L).name("IMAX").address("KOLKATA").city("kolkata").build();
		screen = Screen.builder()
				.id(1L).name("Screen 1").theatre(theatre).build();
		show = Show.builder()
				.id(1L).movie(movie).screen(screen).startTime(LocalDateTime.now()).endTime(LocalDateTime.now().plusHours(2)).ticketPrice(BigDecimal.valueOf(250)).build();
		
		this.testShowRequest = new ShowRequest(
				1L,
				1L,
				LocalDateTime.now(),
				LocalDateTime.now().plusHours(2),
				BigDecimal.valueOf(250)
		);
		
	}
	
	@AfterEach
	void tearDown() {
	}
	
	@Nested
	class create {
		
		@Test
		@DisplayName("Should return true if successfully created")
		void createShow() {
			
			when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
			when(screenRepository.findByIdAndIsActive(1L, true)).thenReturn(Optional.of(screen));
			when(showRepository.findOverlappingShows(
					eq(1L),
					any(LocalDateTime.class),
					any(LocalDateTime.class)
			                                        )).thenReturn(List.of());
			when(showRepository.save(any(Show.class))).thenReturn(show);
			
			final ShowResponse showResponse = ShowServiceImplTest.this.showServiceImpl.create(testShowRequest);
			
			assertNotNull(showResponse);
			assertEquals("japan", showResponse.movieTitle());
			assertEquals("IMAX", showResponse.theatreName());
			assertEquals("Screen 1", showResponse.screenName());
			
			verify(showRepository).save(any(Show.class));
			
		}
		
		@Test
		@DisplayName("Should reject show when start time is after end time")
		void validateTime() {
			ShowRequest testShowRequest = new ShowRequest(
					1L,
					1L,
					LocalDateTime.now().plusHours(3),
					LocalDateTime.now().plusHours(1),
					BigDecimal.valueOf(250)
			);
			
			assertThrows(IllegalArgumentException.class, () ->
					             showServiceImpl.create(testShowRequest)
			            );
			verifyNoInteractions(movieRepository);
			verifyNoInteractions(screenRepository);
			verifyNoInteractions(showRepository);
		}
		
		@Test
		@DisplayName("Reject Inactive Movie")
		void rejectInactiveMovie() {
			movie.setActive(false);
			when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
			when(screenRepository.findByIdAndIsActive(1L, true)).thenReturn(Optional.of(screen));
			ShowRequest testShowRequest = new ShowRequest(
					1L,
					1L,
					LocalDateTime.now().plusDays(1),
					LocalDateTime.now().plusDays(1).plusHours(2),
					new BigDecimal("250.00")
			);
			assertThrows(IllegalArgumentException.class, () ->showServiceImpl.create(testShowRequest));
			verify(showRepository,never()).save(any(Show.class));
		}
		
		@Test
		@DisplayName("Reject Missing movie !")
		void create_shouldRejectMissingMovie() {
			when(movieRepository.findById(1L)).thenReturn(Optional.empty());
			ShowRequest testShowRequest = new ShowRequest(
					1L,
					1L,
					LocalDateTime.now().plusDays(1),
					LocalDateTime.now().plusDays(1).plusHours(2),
					new BigDecimal("250.00")
			);
			assertThrows(ResourceNotFoundException.class, () ->showServiceImpl.create(testShowRequest));
			verify(showRepository,never()).save(any(Show.class));
		}
		
		@Test
		@DisplayName("Reject Inactive Screens")
		void create_shouldRejectInactiveScreen() {
			screen.setActive(false);
			when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
			when(screenRepository.findByIdAndIsActive(1L, true)).thenReturn(Optional.of(screen));
			ShowRequest testShowRequest = new ShowRequest(
					1L,
					1L,
					LocalDateTime.now().plusDays(1),
					LocalDateTime.now().plusDays(1).plusHours(2),
					new BigDecimal("250.00")
			);
			assertThrows(IllegalArgumentException.class, () ->showServiceImpl.create(testShowRequest));
			verify(showRepository,never()).save(any(Show.class));
			
		}
	}
	
	@Nested
	@DisplayName("Delete")
	class delete {
		@Test
		void delete_shouldSoftDeleteShow() {
			
			Show show = Show.builder()
					.id(1L)
					.movie(movie)
					.screen(screen)
					.startTime(LocalDateTime.now().plusDays(1))
					.endTime(LocalDateTime.now().plusDays(1).plusHours(2))
					.ticketPrice(new BigDecimal("250"))
					.isActive(true)
					.build();
			
			when(showRepository.findByIdAndIsActiveTrue(1L))
					.thenReturn(Optional.of(show));
			
			showServiceImpl.delete(1L);
			
			assertFalse(show.isActive());
			
			verify(showRepository).save(show);
		}
	}
	
	
}