package com.mickey.cinebook.repository;

import com.mickey.cinebook.entity.Booking;
import com.mickey.cinebook.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository
		extends JpaRepository<Booking, Long> {
	
	List<Booking> findByUserId(Long userId);
	
	List<Booking> findByUserIdAndStatus(
			Long userId,
			BookingStatus status
	                                   );
}