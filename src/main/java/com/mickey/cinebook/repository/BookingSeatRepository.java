package com.mickey.cinebook.repository;

import com.mickey.cinebook.entity.BookingSeat;
import com.mickey.cinebook.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingSeatRepository
		extends JpaRepository<BookingSeat, Long> {
	
	@Query("""
            SELECT bs.seat.id
            FROM BookingSeat bs
            WHERE bs.booking.show.id = :showId
              AND bs.booking.status IN :statuses
              AND bs.seat.id IN :seatIds
            """)
	List<Long> findBookedSeatIds(
			@Param("showId") Long showId,
			@Param("seatIds") List<Long> seatIds,
			@Param("statuses") List<BookingStatus> statuses
	                            );
	
	List<BookingSeat> findByBookingId(Long bookingId);
}