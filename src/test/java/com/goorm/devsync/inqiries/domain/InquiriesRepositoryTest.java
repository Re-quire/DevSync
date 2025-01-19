package com.goorm.devsync.inqiries.domain;

import com.goorm.devsync.domain.Inquiries;
import com.goorm.devsync.domain.InquiriesRepository;
import com.goorm.devsync.domain.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class InquiriesRepositoryTest {

    @Autowired
    private InquiriesRepository inquiriesRepository;

    @BeforeEach
    void setup() {
        inquiriesRepository.deleteAll();
    }

    @Test
    void saveAndFindInquiry() {
        Inquiries inquiry = Inquiries.builder().title("Test Inquiry").content("Test Inquiry Content").status(Status.PENDING).build();

        Inquiries savedInquiry = inquiriesRepository.save(inquiry);

        assertThat(savedInquiry).isNotNull();
        assertThat(savedInquiry.getId()).isNotNull();
        assertThat(savedInquiry.getTitle()).isEqualTo("Test Inquiry");
        assertThat(savedInquiry.getContent()).isEqualTo("Test Inquiry Content");
        assertThat(savedInquiry.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void updateInquiry() {
        Inquiries inquiry = Inquiries.builder().title("Inital Title").content("Initial Content").status(Status.PENDING).build();

        Inquiries savedInquiry = inquiriesRepository.save(inquiry);

        savedInquiry = inquiriesRepository.findById(savedInquiry.getId()).orElseThrow();
        savedInquiry = Inquiries.builder()
                .title("Updated Title")
                .content("Updated Content")
                .status(Status.RESOLVED)
                .createdAt(savedInquiry.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Inquiries updatedInquiry = inquiriesRepository.save(savedInquiry);

        assertThat(updatedInquiry.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedInquiry.getContent()).isEqualTo("Updated Content");
        assertThat(updatedInquiry.getStatus()).isEqualTo(Status.RESOLVED);
    }

    @Test
    void deleteInquiry() {
        Inquiries inquiry = Inquiries.builder().title("To Be Deleted").content("Delete this inquiry").status(Status.PENDING).build();

        Inquiries savedInquiry = inquiriesRepository.save(inquiry);

        inquiriesRepository.delete(savedInquiry);

        assertThat(inquiriesRepository.findById(savedInquiry.getId())).isEmpty();
    }

    @Test
    void findByStatus() {
        Inquiries inquiry1 = Inquiries.builder().title("Pending Inquiry 1").content("Content 1").status(Status.PENDING).build();
        Inquiries inquiry2 = Inquiries.builder().title("Pending Inquiry 2").content("Content 2").status(Status.PENDING).build();
        Inquiries inquiry3 = Inquiries.builder().title("Pending Inquiry 3").content("Content 3").status(Status.RESOLVED).build();

        inquiriesRepository.save(inquiry1);
        inquiriesRepository.save(inquiry2);
        inquiriesRepository.save(inquiry3);

        Pageable pageable = PageRequest.of(0,10);
        Page<Inquiries> pendingInquiries = inquiriesRepository.findByStatus(Status.PENDING, pageable);

        assertThat(pendingInquiries.getTotalElements()).isEqualTo(2);
        assertThat(pendingInquiries.getContent()).extracting("title").containsExactlyInAnyOrder("Pending Inquiry 1", "Pending Inquiry 2");
    }
}
