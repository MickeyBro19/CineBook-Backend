package com.mickey.cinebook.repository;

import com.mickey.cinebook.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	
	List<Seat> findByIsActiveTrue();
	
	List<Seat> findByScreenIdAndIsActiveTrue(Long screenId);
	
	Optional<Seat> findByIdAndIsActive(Long id, boolean isActive);
	
	boolean existsByScreenIdAndSeatNumberIgnoreCase(
			Long screenId,
			String seatNumber
	                                               );
	
	boolean existsByScreenIdAndSeatNumberIgnoreCaseAndIdNot(
			Long screenId,
			String seatNumber,
			Long id
	                                                       );
}