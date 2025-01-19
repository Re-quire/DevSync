package com.goorm.devsync.inqiries.service;

import com.goorm.devsync.domain.Inquiries;
import com.goorm.devsync.domain.InquiriesRepository;
import com.goorm.devsync.domain.Status;
import com.goorm.devsync.dto.CreateInquiryRequest;
import com.goorm.devsync.dto.InquiryResponse;
import com.goorm.devsync.service.InquiriesService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class InquiriesServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(InquiriesServiceTest.class);

    @Autowired
    private InquiriesService inquiriesService;

    @Autowired
    private InquiriesRepository inquiriesRepository;

    @BeforeEach
    void setUp() {
        inquiriesRepository.deleteAll();
    }

    @Test
    void createInquiry_shouldSaveInquiryToDatabase() {
        logger.info("문의를생성");

        CreateInquiryRequest request = new CreateInquiryRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        logger.debug("Created inquiry request: {}", request);

        InquiryResponse response = inquiriesService.createInquiry(request);
        logger.debug("Received inquiry response: {}", response);


        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Title");
        assertThat(response.getContent()).isEqualTo("Test Content");

        List<Inquiries> inquiriesList = inquiriesRepository.findAll();
        logger.debug("Fetched inquiries from the database: {}", inquiriesList);

        assertThat(inquiriesList.size()).isEqualTo(1);
        assertThat(inquiriesList.get(0).getTitle()).isEqualTo(request.getTitle());
        assertThat(inquiriesList.get(0).getContent()).isEqualTo(request.getContent());

        logger.info("Test Passed: createInquiry");
    }

    @Test
    void getInquiryDetails_shouldReturnCorrectInquiryDetails() {
        logger.info("문의 내용 상세 조회");

        Inquiries inquiry = Inquiries.builder()
                .title("Test Inqiry")
                .content("Test Content")
                .status(Status.PENDING)
                .build();
        logger.debug("Created inquiry: {}", inquiry);

        Inquiries savedInquiry = inquiriesRepository.save(inquiry);
        logger.debug("Saved inquiry to database: {}", savedInquiry);

        InquiryResponse response = inquiriesService.getInquiryDetails(savedInquiry.getId());
        logger.debug("Received inquiry details response: {}", response);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(savedInquiry.getId());
        assertThat(response.getTitle()).isEqualTo(savedInquiry.getTitle());
        assertThat(response.getContent()).isEqualTo(savedInquiry.getContent());

        logger.info("Test Passed: getInquiryDetails");

    }

    @Test
    void getInquiries_shouldReturnAllInquiries() {
        logger.info("모든 문의 조회");

        Inquiries inquiry1 = Inquiries.builder().title("Test 1").content("Test content").status(Status.PENDING).build();
        Inquiries inquiry2 = Inquiries.builder().title("Test 2").content("Test content").status(Status.RESOLVED).build();
        logger.debug("Created inquiries: {}, {}", inquiry1,inquiry2);

        inquiriesRepository.save(inquiry1);
        inquiriesRepository.save(inquiry2);
        logger.debug("Saved inquiries to database");

        List<Inquiries> allInquiries = inquiriesRepository.findAll();
        logger.debug("Fetched all inquiries from database: {}", allInquiries);

        assertThat(allInquiries.size()).isEqualTo(2);
        assertThat(allInquiries.get(0).getTitle()).isEqualTo("Test 1");
        assertThat(allInquiries.get(1).getTitle()).isEqualTo("Test 2");

        logger.info("Test Passed: getAllInquiries");
    }

    @Test
    void deletedInquiry_shouldRemoveInquiryFromDatabase() {
        logger.info("문의 삭제");

        Inquiries inquiry = Inquiries.builder().title("Test title").content("Test content").status(Status.PENDING).build();
        logger.debug("Created inquiry: {}", inquiry);

        Inquiries savedInquiry = inquiriesRepository.save(inquiry);
        logger.debug("Saved inquiry to database: {}", savedInquiry);

        inquiriesService.deletedInquiry(savedInquiry.getId());
        logger.debug("Deleted inquiry with ID: {}", savedInquiry.getId());

        List<Inquiries> inquiriesList = inquiriesRepository.findAll();
        logger.debug("Fetched inquiries after deletion: {}", inquiriesList);

        assertThat(inquiriesList).isEmpty();

        logger.info("Test Passed: deletedInquiry");
    }

}
