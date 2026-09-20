package com.mickey.cinebook.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "theatres",uniqueConstraints = {
		@UniqueConstraint(name = "uk_theatre_name_and_city", columnNames = {"name","city"})
})

public class Theatre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private String city;
	
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
