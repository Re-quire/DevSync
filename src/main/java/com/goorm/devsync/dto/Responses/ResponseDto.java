package com.goorm.devsync.dto.Responses;

import com.goorm.devsync.domain.Responses;
import com.goorm.devsync.domain.Status;
import com.goorm.devsync.dto.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseDto {
    private Long id;
    private Long inquiryId;
    private String content;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResponseDto fromEntity(Responses responses) {
         return ResponseDto.builder()
                 .id(responses.getId())
                 .inquiryId(responses.getInquiry().getId())
                 .content(responses.getContent())
                 .status(responses.getStatus())
                 .createdAt(responses.getCreatedAt())
                 .updatedAt(responses.getUpdatedAt())
                 .build();
    }
}
