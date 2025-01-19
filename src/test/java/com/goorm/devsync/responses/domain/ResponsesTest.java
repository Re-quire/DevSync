package com.goorm.devsync.responses.domain;

import com.goorm.devsync.domain.Inquiries;
import com.goorm.devsync.domain.Responses;
import com.goorm.devsync.domain.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponsesTest {

    @Test
    void shouldCreateResponsesWithValidFields() {
        Inquiries inquiry = Inquiries.builder()
                .title("Test title")
                .content("This is a test inquiry")
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        Responses responses = Responses.builder()
                .content("Test Response Content")
                .status(Status.RESOLVED)
                .inquiry(inquiry)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        assertThat(responses).isNotNull();
        assertThat(responses.getContent()).isEqualTo("Test Response Content");
        assertThat(responses.getStatus()).isEqualTo(Status.RESOLVED);
        assertThat(responses.getInquiry()).isEqualTo(inquiry);
        assertThat(responses.getCreatedAt()).isEqualTo(createdAt);
        assertThat(responses.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void shouldUpdateContentAndResetUpdatedAt() {

        LocalDateTime initialUpdatedAt = LocalDateTime.now().minusHours(1);
        Responses responses = Responses.builder()
                .content("Old response content")
                .status(Status.PENDING)
                .inquiry(new Inquiries())
                .createdAt(LocalDateTime.now())
                .updatedAt(initialUpdatedAt)
                .build();

        responses.updateContent("New response content");

        assertThat(responses.getContent()).isEqualTo("New response content");
        assertThat(responses.getUpdatedAt()).isAfter(initialUpdatedAt);
    }

    @Test
    void shouldUpdateStatus() {
        Responses responses = Responses.builder()
                .content("Response content")
                .status(Status.PENDING)
                .inquiry(new Inquiries())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        responses.setStatus(Status.RESOLVED);

        assertThat(responses.getStatus()).isEqualTo(Status.RESOLVED);
    }
}
