package com.goorm.devsync.responses.service;

import com.goorm.devsync.domain.*;
import com.goorm.devsync.dto.Responses.CreateResponseRequest;
import com.goorm.devsync.service.Responses.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ResponseServiceTest {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private InquiriesRepository inquiriesRepository;

    private Inquiries inquiry;

    @BeforeEach
    void setup() {
        inquiry = Inquiries.builder()
                .title("Test Inquiry")
                .content("This is a test inquiry")
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        inquiriesRepository.save(inquiry);
    }

    @Test
    void getResponseByInquiryId() {
        Responses response1 = Responses.builder().inquiry(inquiry).content("문의 답변").status(Status.PENDING).createdAt(LocalDateTime.of(2025,1,17,10,0)).build();

        Responses response2 = Responses.builder().inquiry(inquiry).content("문의 답변2").status(Status.PENDING).createdAt(LocalDateTime.of(2025,1,17,13,0)).build();

        responseRepository.save(response1);
        responseRepository.save(response2);

        Responses response = responseService.getResponseByInquiryId(inquiry.getId());

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("문의 답변2");
    }

    @Test
    void getResponseByInquiryId_SholdThrowExecptionWhenNoResponses() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->
            responseService.getResponseByInquiryId(inquiry.getId()));

        assertThat(exception.getMessage()).contains("해당 문의에 대한 답변이 없습니다.");
    }

    @Test
    void createResponse_ShouldSaveAndReturnResponse() {
        CreateResponseRequest request = new CreateResponseRequest();
        request.setContent("Test Response");

        Responses response = responseService.createaResponse(inquiry.getId(), request);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("Test Response");
        assertThat(responseRepository.findByInquiryId(inquiry.getId())).contains(response);
    }

    @Test
    void updateResponse_ShouldUpdateResponse() {

        Responses savedResponse = Responses.builder().inquiry(inquiry).content("Original content").status(Status.PENDING).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        responseRepository.save(savedResponse);

        CreateResponseRequest request = new CreateResponseRequest();
        request.setContent("Updated content");

        Responses updatedResponse = responseService.updateResponse(inquiry.getId(),savedResponse.getId(), request);

        assertThat(updatedResponse).isNotNull();
        assertThat(updatedResponse.getContent()).isEqualTo("Updated content");
    }

    @Test
    void updateResponseStatus_ShouldChangeStatusAndResturnResponse() {
        
        Responses savedResonse = Responses.builder()
                .inquiry(inquiry)
                .content("Response content")
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        responseRepository.save(savedResonse);
        
        Responses updatedResponse = responseService.updateResponseStatus(inquiry.getId(), savedResonse.getId(), Status.RESOLVED);
        
        assertThat(updatedResponse).isNotNull();
        assertThat(updatedResponse.getStatus()).isEqualTo(Status.RESOLVED);
        
    }
    
    @Test
    void addResponseToInquiry_ShouldSaveAndReturnResponse() {
        
        String content = "New response content";
        Status status = Status.PENDING;
        
        Responses response = responseService.addResponseToInquiry(inquiry.getId(), content, status);
        
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(responseRepository.findByInquiryId(inquiry.getId())).contains(response);
    }
    
    @Test
    void getAllResponseForInqiry_ShouldReturnAllReseponse() {

        Responses response1 = Responses.builder().inquiry(inquiry).content("Response 1").status(Status.PENDING).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        Responses response2 = Responses.builder().inquiry(inquiry).content("Response 2").status(Status.PENDING).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        responseRepository.save(response1);
        responseRepository.save(response2);

        List<Responses> responses = responseService.getAllResponsesForInquiry(inquiry.getId());

        assertThat(responses).hasSize(2);
        assertThat(responses).contains(response1, response2);
    }
}
