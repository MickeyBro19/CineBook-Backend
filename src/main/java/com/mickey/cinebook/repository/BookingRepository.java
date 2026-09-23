package com.mickey.cinebook.repository;

import com.mickey.cinebook.entity.Booking;
import com.mickey.cinebook.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository
		extends JpaRepository<Booking, Long> {
	
	Page<Booking> findByUserId(Long userId, Pageable pageable);
	
	List<Booking> findByUserIdAndStatus(
			Long userId,
			BookingStatus status
	                                   );
}