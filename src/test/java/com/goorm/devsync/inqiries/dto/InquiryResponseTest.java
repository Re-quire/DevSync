package com.goorm.devsync.inqiries.dto;

import com.goorm.devsync.dto.InquiryResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InquiryResponseTest {

    @Test
    void inquiryResponseFieldsShouldSetAndReturnCorrectValues() {
        Long id = 1L;
        String title = "Test title";
        String content = "Test content";

        InquiryResponse inquiryResponse = new InquiryResponse();
        inquiryResponse.setId(id);
        inquiryResponse.setTitle(title);
        inquiryResponse.setContent(content);

        assertThat(inquiryResponse.getId()).isEqualTo(1L);
        assertThat(inquiryResponse.getTitle()).isEqualTo("Test title");
        assertThat(inquiryResponse.getContent()).isEqualTo("Test content");
    }

    @Test
    void inquiryResponseShouldFollowEqualsAndHashCodeContract() {
        InquiryResponse response1 = new InquiryResponse();
        response1.setId(1L);
        response1.setTitle("Test title");
        response1.setContent("Test content");

        InquiryResponse response2 = new InquiryResponse();
        response2.setId(1L);
        response2.setTitle("Test title");
        response2.setContent("Test content");

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }
}
