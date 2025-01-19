package com.goorm.devsync.responses.domain;

import com.goorm.devsync.domain.*;
import com.goorm.devsync.dto.InquiryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ResponseRepositoryTest {

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private InquiriesRepository inquiriesRepository;

    private Inquiries savedInquiry;

    @BeforeEach
    void setUp() {
        Inquiries inquiry = Inquiries.builder()
                .title("Test Inquiry Title")
                .content("Test Inquiry Content")
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savedInquiry = inquiriesRepository.save(inquiry);
    }

    @Test
    void shouldSaveAndRetrievResponse() {

        Responses respons = Responses.builder()
                .content("Test Response Content")
                .status(Status.RESOLVED)
                .inquiry(savedInquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Responses savedResponse = responseRepository.save(respons);

        Optional<Responses> retrievedResponse = responseRepository.findById(savedResponse.getId());
        assertThat(retrievedResponse).isPresent();
        assertThat(retrievedResponse.get().getContent()).isEqualTo("Test Response Content");
        assertThat(retrievedResponse.get().getStatus()).isEqualTo(Status.RESOLVED);
        assertThat(retrievedResponse.get().getInquiry()).isEqualTo(savedInquiry);
    }

    @Test
    void shouldFindResponsesByInquiryId() {
        Responses response1 = Responses.builder()
                .content("Response Content 1")
                .status(Status.PENDING)
                .inquiry(savedInquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Responses response2 = Responses.builder()
                .content("Response Content 2")
                .status(Status.RESOLVED)
                .inquiry(savedInquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        responseRepository.save(response1);
        responseRepository.save(response2);

        List<Responses> responses = responseRepository.findByInquiryId(savedInquiry.getId());

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getContent()).isEqualTo("Response Content 1");
        assertThat(responses.get(1).getContent()).isEqualTo("Response Content 2");
    }

    @Test
    void shouldDeleteResponse() {
        Responses response = Responses.builder()
                .content("Response to delete")
                .status(Status.PENDING)
                .inquiry(savedInquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Responses savedResponse = responseRepository.save(response);

        responseRepository.delete(savedResponse);

        Optional<Responses> deleteResponse = responseRepository.findById(savedResponse.getId());
        assertThat(deleteResponse).isEmpty();
    }
}
