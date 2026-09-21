package com.mickey.cinebook.entity;

import com.mickey.cinebook.entity.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
		name = "seats",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_screen_seat_number",
						columnNames = {"screen_id", "seat_number"}
				)
		}
)
public class Seat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "seat_number", nullable = false, length = 10)
	private String seatNumber;
	
	@Column(name = "row_label", nullable = false, length = 5)
	private String rowLabel;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private SeatType seatType;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "screen_id", nullable = false)
	private Screen screen;
	
	@Builder.Default
	@Column(nullable = false)
	private boolean isActive = true;
	
	@Column(nullable = false, updatable = false)
	private Instant createdAt;
	
	@Column(nullable = false)
	private Instant updatedAt;
	
	@PrePersist
	public void prePersist() {
		createdAt = Instant.now();
		updatedAt = Instant.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedAt = Instant.now();
	}
}