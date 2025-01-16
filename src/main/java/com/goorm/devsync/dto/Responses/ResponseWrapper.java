package com.goorm.devsync.dto.Responses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseWrapper<T> {

    private String message;
    private T response;
}
