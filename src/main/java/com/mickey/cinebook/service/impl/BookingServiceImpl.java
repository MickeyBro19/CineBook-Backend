package com.mickey.cinebook.service.impl;

import com.mickey.cinebook.auth.entity.User;
import com.mickey.cinebook.auth.repository.UserRepository;
import com.mickey.cinebook.dto.BookingRequest;
import com.mickey.cinebook.dto.BookingResponse;
import com.mickey.cinebook.entity.*;
import com.mickey.cinebook.entity.enums.BookingStatus;
import com.mickey.cinebook.exception.ResourceNotFoundException;
import com.mickey.cinebook.repository.BookingRepository;
import com.mickey.cinebook.repository.BookingSeatRepository;
import com.mickey.cinebook.repository.SeatRepository;
import com.mickey.cinebook.repository.ShowRepository;
import com.mickey.cinebook.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	
	private final BookingRepository bookingRepository;
	private final BookingSeatRepository bookingSeatRepository;
	private final ShowRepository showRepository;
	private final SeatRepository seatRepository;
	private final UserRepository userRepository;
	
	@Override
	@Transactional
	public BookingResponse create(BookingRequest request) {
		
		User user = getCurrentUser();
		
		Show show = showRepository.findByIdAndIsActiveTrue(request.showId())
				.orElseThrow(() ->
						             new ResourceNotFoundException("Show not found"));
		
		List<Long> seatIds = request.seatIds();
		
		if (seatIds.stream().distinct().count() != seatIds.size()) {
			throw new IllegalArgumentException(
					"Duplicate seats are not allowed"
			);
		}
		
		List<Seat> seats = seatRepository.findAllById(seatIds);
		
		if (seats.size() != seatIds.size()) {
			throw new ResourceNotFoundException(
					"One or more seats not found"
			);
		}
		
		for (Seat seat : seats) {
			
			if (!seat.isActive()) {
				throw new IllegalArgumentException(
						"Seat " + seat.getSeatNumber() + " is inactive"
				);
			}
			
			if (seat.getScreen().getId()!=(show.getScreen().getId())) {
				
				throw new IllegalArgumentException(
						"Seat " + seat.getSeatNumber()
								+ " does not belong to this screen"
				);
			}
		}
		
		List<BookingStatus> activeStatuses =
				List.of(
						BookingStatus.PENDING,
						BookingStatus.CONFIRMED
				       );
		
		List<Long> bookedSeatIds =
				bookingSeatRepository.findBookedSeatIds(
						show.getId(),
						seatIds,
						activeStatuses
				                                       );
		
		if (!bookedSeatIds.isEmpty()) {
			
			throw new IllegalArgumentException(
					"One or more selected seats are already booked"
			);
		}
		
		BigDecimal totalAmount =
				show.getTicketPrice()
						.multiply(BigDecimal.valueOf(seats.size()));
		
		Booking booking = Booking.builder()
				.user(user)
				.show(show)
				.totalAmount(totalAmount)
				.status(BookingStatus.PENDING)
				.build();
		
		bookingRepository.save(booking);
		
		List<BookingSeat> bookingSeats = seats.stream()
				.map(seat -> BookingSeat.builder()
						.booking(booking)
						.seat(seat)
						.build())
				.toList();
		
		bookingSeatRepository.saveAll(bookingSeats);
		
		return toResponse(booking, seats);
	}
	
	@Override
	@Transactional(readOnly = true)
	public BookingResponse findById(Long id) {
		
		User user = getCurrentUser();
		
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() ->
						             new ResourceNotFoundException("Booking not found"));
		
		if (!booking.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException(
					"You are not allowed to access this booking"
			);
		}
		
		List<Seat> seats = bookingSeatRepository
				.findByBookingId(booking.getId())
				.stream()
				.map(BookingSeat::getSeat)
				.toList();
		
		return toResponse(booking, seats);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<BookingResponse> findMyBookings(Pageable pageable) {
		
		User user = getCurrentUser();
		Page<Booking> bookings= bookingRepository.findByUserId(user.getId(), pageable);
		
		return bookings
				.map(booking -> {
					
					List<Seat> seats = bookingSeatRepository
							.findByBookingId(booking.getId())
							.stream()
							.map(BookingSeat::getSeat)
							.toList();
					
					return toResponse(booking, seats);
				});
	}
	
	@Override
	@Transactional
	public void cancel(Long id) {
		
		User user = getCurrentUser();
		
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() ->
						             new ResourceNotFoundException(
								             "Booking not found"
						             ));
		
		if (!booking.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException(
					"You are not allowed to cancel this booking"
			);
		}
		
		if (booking.getStatus() == BookingStatus.CANCELLED) {
			throw new IllegalArgumentException(
					"Booking is already cancelled"
			);
		}
		
		booking.setStatus(BookingStatus.CANCELLED);
	}
	
	private User getCurrentUser() {
		
		String email = SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getName();
		
		return userRepository.findByEmail(email)
				.orElseThrow(() ->
						             new ResourceNotFoundException(
								             "Authenticated user not found"
						             ));
	}
	
	private BookingResponse toResponse(
			Booking booking,
			List<Seat> seats
	                                  ) {
		
		Show show = booking.getShow();
		Screen screen = show.getScreen();
		Theatre theatre = screen.getTheatre();
		
		return new BookingResponse(
				booking.getId(),
				booking.getUser().getId(),
				show.getId(),
				show.getMovie().getTitle(),
				theatre.getName(),
				screen.getName(),
				seats.stream()
						.map(Seat :: getId)
						.toList(),
				booking.getTotalAmount(),
				booking.getStatus(),
				booking.getCreatedAt()
		);
	}
}