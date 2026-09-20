package com.mickey.cinebook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.time.Instant;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "screens")
@AllArgsConstructor
public class Screen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true,nullable = false,length = 100)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theatre_id",nullable = false,unique = true)
	private Theatre theatre;
	
	@Column(unique = true,nullable = false)
	@Builder.Default
	private boolean isActive=true;
	
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
