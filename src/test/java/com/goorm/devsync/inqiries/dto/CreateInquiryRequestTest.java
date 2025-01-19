package com.goorm.devsync.inqiries.dto;

import com.goorm.devsync.dto.CreateInquiryRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateInquiryRequestTest {

    @Test
    void createInquiryRequestFieldsShouldSetAndReturnCorrectValues() {

        String title = "Test title";
        String content = "Test content";

        CreateInquiryRequest request = new CreateInquiryRequest();
        request.setTitle(title);
        request.setContent(content);

        assertThat(request.getTitle()).isEqualTo("Test title");
        assertThat(request.getContent()).isEqualTo("Test content");
    }

    @Test
    void createInquiryRequestShouldFollowEqualsAndHashCodeContract() {
        CreateInquiryRequest request1 = new CreateInquiryRequest();
        request1.setTitle("Test title");
        request1.setContent("Test content");

        CreateInquiryRequest request2 = new CreateInquiryRequest();
        request2.setTitle("Test title");
        request2.setContent("Test content");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }
}
