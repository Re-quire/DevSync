package com.goorm.devsync.dto.Responses;

import com.goorm.devsync.domain.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateResponseRequest {

    @NotBlank(message = "답변 내용은 필수입니다.")
    @Size(min = 1, max = 2000, message = "답변 내용은 1자 이상 2000자 이하여야 합니다.")
    private String content;

}
