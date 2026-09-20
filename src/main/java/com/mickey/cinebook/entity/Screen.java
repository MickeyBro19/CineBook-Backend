package com.mickey.cinebook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "screens", uniqueConstraints = {
		@UniqueConstraint(name = "uk_theatreId_and_screenName", columnNames = {"theatre_id", "name"})
})
@AllArgsConstructor
public class Screen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theatre_id", nullable = false)
	private Theatre theatre;
	
	@Column(nullable = false)
	@Builder.Default
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
