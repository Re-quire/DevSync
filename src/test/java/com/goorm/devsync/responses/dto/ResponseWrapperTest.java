package com.goorm.devsync.responses.dto;

import com.goorm.devsync.domain.Responses;
import com.goorm.devsync.domain.Status;
import com.goorm.devsync.dto.Responses.ResponseWrapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseWrapperTest {

    @Test
    void responseWrapper() {
        String message = "Success";
        Responses responses = Responses.builder()
                .content("test response content")
                .status(Status.PENDING)
                .build();

        ResponseWrapper<Responses> responsesResponseWrapper = ResponseWrapper.<Responses>builder()
                .message(message)
                .response(responses)
                .build();

        assertThat(responsesResponseWrapper).isNotNull();
        assertThat(responsesResponseWrapper.getMessage()).isEqualTo(message);
        assertThat(responsesResponseWrapper.getResponse()).isEqualTo(responses);
        assertThat(responsesResponseWrapper.getResponse().getContent()).isEqualTo("test response content");
    }

    @Test
    void responseWrapper_ShouldHandleNullResponse() {
        String message = "Empty response";

        ResponseWrapper<Responses> responseWrapper = ResponseWrapper.<Responses>builder()
                .message(message)
                .response(null)
                .build();

        assertThat(responseWrapper).isNotNull();
        assertThat(responseWrapper.getMessage()).isEqualTo(message);
        assertThat(responseWrapper.getResponse()).isNull();
    }
}
