package com.goorm.devsync.service.Responses;

import com.goorm.devsync.domain.*;
import com.goorm.devsync.dto.Responses.CreateResponseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final InquiriesRepository inquiriesRepository;


    public Responses getResponseByInquiryId(Long inquiryId) {
        Inquiries inquiry = inquiriesRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의를 찾을 수 없습니다. ID: " + inquiryId));

        List<Responses> responses = responseRepository.findByInquiryId(inquiry.getId());

        if (responses.isEmpty()) {
            throw new IllegalArgumentException("해당 문의에 대한 답변이 없습니다. ID: " + inquiryId);
        }

        return responses.stream().max((r1,r2) -> r1.getCreatedAt().compareTo(r2.getCreatedAt()))
                .orElseThrow(()->new IllegalArgumentException("해당 문의에 대한 답변이 없습니다. ID: " + inquiryId));
    }

    public Responses createaResponse(Long inquiryId, CreateResponseRequest request) {
        Inquiries inquiry = inquiriesRepository.findById(inquiryId).orElseThrow(()->new IllegalArgumentException("문의가 존재하지 않습니다. ID: "+inquiryId));

        Responses response = Responses.builder()
                .inquiry(inquiry)
                .content(request.getContent())
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return responseRepository.save(response);
    }

    public Responses updateResponse(Long inquiryId, Long responseId, CreateResponseRequest request) {
        inquiriesRepository.findById(inquiryId)
                .orElseThrow(()-> new IllegalArgumentException("문의가 존재하지 않습니다. IO: "+inquiryId));

        Responses responses = responseRepository.findById(responseId)
                .orElseThrow(()->new IllegalArgumentException("해당 답변을 찾으 수 없습니다. ID: "+responseId));

        responses.updateContent(request.getContent());

        return responseRepository.save(responses);
    }

    public Responses updateResponseStatus(Long inquiryId, Long responseId, Status newStatus) {

        inquiriesRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의를 찾을 수 없습니다. ID: " + inquiryId));

        Responses response = responseRepository.findById(responseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 답변을 찾을 수 없습니다. ID: " + responseId));

        response.setStatus(Status.valueOf(newStatus.name()));
        response.setUpdatedAt(LocalDateTime.now());

        return responseRepository.save(response);
    }

    public Responses addResponseToInquiry(Long inquiryId, String content, Status status) {
        Inquiries inquiry = inquiriesRepository.findById(inquiryId)
                .orElseThrow(()-> new IllegalArgumentException("해당 문의를 찾을 수 없습니다. ID: " + inquiryId));

        Responses response = Responses.builder()
                .content(content)
                .status(status)
                .inquiry(inquiry)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return responseRepository.save(response);
    }

    public List<Responses> getAllResponsesForInquiry(Long inquiryId) {
        return responseRepository.findByInquiryId(inquiryId);
    }
}
