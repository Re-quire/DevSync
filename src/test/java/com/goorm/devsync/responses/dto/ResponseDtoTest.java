package com.goorm.devsync.responses.dto;

import com.goorm.devsync.domain.Inquiries;
import com.goorm.devsync.domain.Responses;
import com.goorm.devsync.domain.Status;
import com.goorm.devsync.dto.Responses.ResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseDtoTest {

    @Test
    void fromEntity_ShouldCreateResponseDtoFromResponsesEntity() {
        Inquiries inquiry = Inquiries.builder()
                .title("Test title")
                .content("This is a test inquiry")
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Responses responses = Responses.builder()
                .content("Test Response Content")
                .status(Status.RESOLVED)
                .inquiry(inquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ResponseDto responseDto = ResponseDto.fromEntity(responses);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(responses.getId());
        assertThat(responseDto.getInquiryId()).isEqualTo(inquiry.getId());
        assertThat(responseDto.getContent()).isEqualTo("Test Response Content");
        assertThat(responseDto.getStatus()).isEqualTo(responses.getStatus());
        assertThat(responseDto.getCreatedAt()).isEqualTo(responses.getCreatedAt());
        assertThat(responseDto.getUpdatedAt()).isEqualTo(responses.getUpdatedAt());
    }
}
