package com.goorm.devsync.dto;

import com.goorm.devsync.domain.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@Setter
public class InquiryResponse {
    private Long id;
    private String title;
    private String content;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse user;
}
