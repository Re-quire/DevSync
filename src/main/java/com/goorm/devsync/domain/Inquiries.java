package com.goorm.devsync.domain;

import java.time.LocalDateTime;

import com.goorm.devsync.domain.user.User;
import com.goorm.devsync.dto.CreateInquiryRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "inquiries")
public class Inquiries {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min = 1, max = 100, message = "제목은 1자에서 100자 사이")
	@Column(nullable = false)
	private String title;

	@NotBlank
	@Size(min = 1, max = 1000, message = "내용은 1자에서 1000자 사이")
	@Column(nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	private User user;



	@Builder
	public Inquiries(String title, String content, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, User user) {
		this.title = title;
		this.content = content;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.user = user;
	}

}
