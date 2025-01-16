package com.goorm.devsync.controller;

import com.goorm.devsync.domain.Responses;
import com.goorm.devsync.domain.Status;
import com.goorm.devsync.dto.Responses.CreateResponseRequest;
import com.goorm.devsync.dto.Responses.ResponseDto;
import com.goorm.devsync.dto.Responses.ResponseWrapper;
import com.goorm.devsync.service.Responses.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class ResponseController {

    private final ResponseService responseService;

    @GetMapping("/{inquiryId}/responses")
    public ResponseEntity<ResponseWrapper<List<ResponseDto>>> getResponseByInquiryId(@PathVariable Long inquiryId) {

        List<Responses> response = responseService.getAllResponsesForInquiry(inquiryId);

        List<ResponseDto> responseDtos = response.stream()
                .map(ResponseDto::fromEntity)
                .toList();

        ResponseWrapper<List<ResponseDto>> wrapper = ResponseWrapper.<List<ResponseDto>>builder()
                .message("문의에 대한 모든 응답이 성공적으로 조회되었습니다.")
                .response(responseDtos)
                .build();

        return ResponseEntity.ok(wrapper);
    }

    @PostMapping("/{inquiryId}/responses")
    public ResponseEntity<ResponseWrapper<ResponseDto>> createResponse(@PathVariable Long inquiryId, @RequestBody @Valid CreateResponseRequest request) {

        var response = responseService.createaResponse(inquiryId, request);

        var responseDto = ResponseDto.fromEntity(response);

        var wrapper = ResponseWrapper.<ResponseDto>builder()
                .message("문의에 대한 답변이 성공적으로 등록되었습니다.")
                .response(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(wrapper);
    }

    @PutMapping("/{inquiryId}/responses/{responseId}")
    public ResponseEntity<ResponseWrapper<ResponseDto>> updateResponse(@PathVariable Long inquiryId, @PathVariable Long responseId, @RequestBody @Valid CreateResponseRequest request) {
        Responses updatedResponse = responseService.updateResponse(inquiryId,responseId,request);

        ResponseDto responseDto = ResponseDto.fromEntity(updatedResponse);

        ResponseWrapper<ResponseDto> wrapper = ResponseWrapper.<ResponseDto>builder()
                .message("문의 답변이 성공적으로 수정되었습니다.")
                .response(responseDto)
                .build();

        return ResponseEntity.ok(wrapper);
    }

    @PutMapping("/{inquiryId}/responses/{responseId}/status")
    public ResponseEntity<Map<String, Object>> updateResponseStatus(
            @PathVariable Long inquiryId, @PathVariable Long responseId, @RequestBody Map<String, String> statusUpdateRequest) {
        String status = statusUpdateRequest.get("status");

        Status statusEnum = Status.valueOf(status.toUpperCase());

        Responses updatedResponse = responseService.updateResponseStatus(inquiryId, responseId, statusEnum);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "문의 답변 상태가 성공적으로 변경되었습니다.");
        response.put("response", ResponseDto.fromEntity(updatedResponse));

        return ResponseEntity.ok(response);
    }

}
