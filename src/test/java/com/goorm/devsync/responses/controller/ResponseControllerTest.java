package com.goorm.devsync.responses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.devsync.domain.*;
import com.goorm.devsync.dto.Responses.CreateResponseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ResponseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private InquiriesRepository inquiriesRepository;

    private Inquiries inquiry;

    @BeforeEach
    void setUp() {
        responseRepository.deleteAll();
        inquiriesRepository.deleteAll();

        inquiry = inquiriesRepository.save(Inquiries.builder()
                .title("Test Inquiry")
                .content("Test Inquiry Content")
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Test
    void shouldGetResponsesByInquiryId() throws Exception {
        Responses response = responseRepository.save(Responses.builder()
                .content("Test Response Content")
                .status(Status.RESOLVED)
                .inquiry(inquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        mockMvc.perform(get("/api/inquiries/{inquiryId}/responses",inquiry.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("문의에 대한 모든 응답이 성공적으로 조회되었습니다."))
                .andExpect(jsonPath("$.response",hasSize(1)))
                .andExpect(jsonPath("$.response[0].content").value("Test Response Content"))
                .andExpect(jsonPath("$.response[0].status").value("RESOLVED"));
    }

    @Test
    void shouldCreateResponse() throws Exception {

        CreateResponseRequest createRequest = new CreateResponseRequest();
        createRequest.setContent("New response content");

        mockMvc.perform(post("/api/inquiries/{inquiryId}/responses",inquiry.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("문의에 대한 답변이 성공적으로 등록되었습니다."))
                .andExpect(jsonPath("$.response.content").value("New response content"))
                .andExpect(jsonPath("$.response.status").value("PENDING"));

        List<Responses> responses = responseRepository.findByInquiryId(inquiry.getId());
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getContent()).isEqualTo("New response content");
    }

    @Test
    void shouldUpdateResponse() throws Exception {
        Responses responses = responseRepository.save(Responses.builder()
                .content("Old Response Content")
                .status(Status.PENDING)
                .inquiry(inquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        CreateResponseRequest updateRequest = new CreateResponseRequest();
        updateRequest.setContent("Updated Response Content");

        mockMvc.perform(put("/api/inquiries/{inquiryId}/responses/{responseId}",inquiry.getId(),responses.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("문의 답변이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.response.content").value("Updated Response Content"));

        Responses updatedResponse = responseRepository.findById(responses.getId()).orElseThrow();
        assertThat(updatedResponse.getContent()).isEqualTo("Updated Response Content");
    }

    @Test
    void shouldUpdateResponseStatus() throws Exception {
        Responses response = responseRepository.save(Responses.builder()
                .content("Response Content")
                .status(Status.PENDING)
                .inquiry(inquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        String statusUpdateRequest = objectMapper.writeValueAsString(
                new LinkedHashMap<String,String>() {{
                    put("status", "RESOLVED");
                }}
        );

        mockMvc.perform(put("/api/inquiries/{inquiryId}/responses/{responseId}/status",inquiry.getId(),response.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(statusUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("문의 답변 상태가 성공적으로 변경되었습니다."))
                .andExpect(jsonPath("$.response.status").value("RESOLVED"));

        Responses updatedResponse = responseRepository.findById(response.getId()).orElseThrow();
        assertThat(updatedResponse.getStatus()).isEqualTo(Status.RESOLVED);
    }


}
