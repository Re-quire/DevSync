package com.goorm.devsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInquiryRequest {

    @NotBlank
    @Size(min = 1,max = 100, message = "제목은 1자에서 100자 사이")
    private String title;

    @NotBlank
    @Size(min = 1,max = 1000, message = "내용은 1자에서 1000자 사이")
    private String content;
}
