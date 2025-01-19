package com.goorm.devsync.inqiries.controller;

import com.goorm.devsync.domain.Inquiries;
import com.goorm.devsync.domain.InquiriesRepository;
import com.goorm.devsync.domain.Status;
import com.goorm.devsync.domain.user.User;
import com.goorm.devsync.domain.user.UserRepository;
import com.goorm.devsync.dto.CreateInquiryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InquiriesControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(InquiriesControllerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private InquiriesRepository inquiriesRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        inquiriesRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .name("TestUser")
                .build();
        userRepository.save(testUser);
    }

    @Test
    void testCreateInquiry() {
        logger.info("Starting testCreateInquiry");

        String title = "문의 제목";
        String content = "문의 내용";

        CreateInquiryRequest request = new CreateInquiryRequest();
        request.setTitle(title);
        request.setContent(content);

        String url = "http://localhost:" + port + "/api/inquiries";
        logger.info("URL: "+url);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, new HttpEntity<>(request),Void.class);
        logger.info("Received response: " + response.getStatusCode());

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        List<Inquiries> found = inquiriesRepository.findAll();
        logger.info("Found inquiries: " + found.size());

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getTitle()).isEqualTo(title);
        assertThat(found.get(0).getContent()).isEqualTo(content);
        assertThat(found.get(0).getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void testDeleteInquiry() {
        logger.info("Starting testDeleteInquiry");

        Inquiries inquiry = Inquiries.builder()
                .title("Test Inqiry")
                .content("Test Content")
                .status(Status.PENDING)
                .build();

        inquiriesRepository.save(inquiry);
        logger.info("Inquiry saved with ID: "+inquiry.getId());

        String url = "http://localhost:" + port + "/api/inquiries/" + inquiry.getId();
        logger.info("URL: "+url);

        restTemplate.delete(url);
        logger.info("DELETE request completed");

        List<Inquiries> foundAfterDelete = inquiriesRepository.findAll();
        logger.info("Found inquiries after delete: " + foundAfterDelete.size());

        assertThat(foundAfterDelete.size()).isEqualTo(0);
    }
}
