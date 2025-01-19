package com.goorm.devsync.inqiries.domain;

import com.goorm.devsync.domain.Inquiries;
import com.goorm.devsync.domain.InquiriesRepository;
import com.goorm.devsync.domain.Status;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class InquiriesTest {

    @Autowired
    private InquiriesRepository inquiriesRepository;

    @BeforeEach
    void setUp() {
        inquiriesRepository.deleteAll();
    }

    @Test
    void shouldSaveEntityWithValid() {
        Inquiries inquiries = Inquiries.builder().title("Valid Title").content("Valid Content").status(Status.PENDING).build();

        Inquiries savedInquiry = inquiriesRepository.save(inquiries);

        assertThat(savedInquiry.getId()).isNotNull();
        assertThat(savedInquiry.getTitle()).isEqualTo("Valid Title");
        assertThat(savedInquiry.getContent()).isEqualTo("Valid Content");
        assertThat(savedInquiry.getStatus()).isEqualTo(Status.PENDING);
        assertThat(savedInquiry.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(savedInquiry.getUpdatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void shouldNotSaveEntityWithInvalidTitle() {
        Inquiries invalidInquiry = Inquiries.builder().title("").content("Valid Content").status(Status.PENDING).build();

        assertThatThrownBy(()-> inquiriesRepository.saveAndFlush(invalidInquiry))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("제목은 1자에서 100자 사이");
    }

    @Test
    void shouldNotSaveEntityWithTooLongContent() {
        String tooLongContent = "tooLongContent".repeat(1001);

        Inquiries invalidInquiry = Inquiries.builder().title("Valid Title").content(tooLongContent).status(Status.PENDING).build();

        assertThatThrownBy(()-> inquiriesRepository.saveAndFlush(invalidInquiry))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("내용은 1자에서 1000자 사이");
    }
}
