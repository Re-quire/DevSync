package com.goorm.devsync.controller;

import com.goorm.devsync.domain.Status;
import com.goorm.devsync.dto.CreateInquiryRequest;
import com.goorm.devsync.dto.InquiryResponse;
import com.goorm.devsync.service.InquiriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiriesController {

    private final InquiriesService inquiriesService;

    @GetMapping
    public ResponseEntity<Page<InquiryResponse>> getInquiries(
            @RequestParam(required = false) Status status, Pageable pageable
            ) {

        pageable = PageRequest.of(1 - 1, 10);

        Page<InquiryResponse> responses = inquiriesService.getInquiries(status,pageable);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<InquiryResponse> createInquiry(@RequestBody CreateInquiryRequest request) {
        InquiryResponse response = inquiriesService.createInquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponse> getInquiryDetails(@PathVariable Long inquiryId) {
        InquiryResponse response = inquiriesService.getInquiryDetails(inquiryId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Void> deletedInquiry(@PathVariable Long inquiryId) {
        inquiriesService.deletedInquiry(inquiryId);
        return ResponseEntity.noContent().build();
    }
}
