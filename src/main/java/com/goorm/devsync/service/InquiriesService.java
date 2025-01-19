package com.goorm.devsync.service;

import com.goorm.devsync.domain.Inquiries;
import com.goorm.devsync.domain.InquiriesRepository;
import com.goorm.devsync.domain.Status;
import com.goorm.devsync.domain.user.User;
import com.goorm.devsync.domain.user.UserRepository;
import com.goorm.devsync.dto.CreateInquiryRequest;
import com.goorm.devsync.dto.InquiryResponse;
import com.goorm.devsync.dto.UserResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiriesService {

    private final InquiriesRepository inquiriesRepository;
    private final UserRepository userRepository;

    public InquiryResponse createInquiry(CreateInquiryRequest request) {
//        User user = getCurrentUser();

        Inquiries inquiry = Inquiries.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
              //  .user(user)
                .build();

        Inquiries savedInquiry = inquiriesRepository.save(inquiry);

        return toResponse(savedInquiry);
    }

    public InquiryResponse getInquiryDetails(Long inquiryId) {
        Inquiries inquiry = inquiriesRepository.findById(inquiryId).orElseThrow(()->new IllegalArgumentException("Inquiry not found with ID : "+inquiryId));
        return toResponse(inquiry);
    }

   public Page<InquiryResponse> getInquiries(Status status, Pageable pageable) {
        if (status != null) {
            return inquiriesRepository.findByStatus(status, pageable).map(this::toResponse);
        }

        return inquiriesRepository.findAll(pageable).map(this::toResponse);
   }

   public void deletedInquiry(Long inquiryId) {
        Inquiries inquiry = inquiriesRepository.findById(inquiryId)
                .orElseThrow(()->new IllegalArgumentException("문의가 존재하지 않습니다. ID: "+inquiryId));

        inquiriesRepository.delete(inquiry);
   }

    private InquiryResponse toResponse(Inquiries inquiry) {
        InquiryResponse response = new InquiryResponse();
        response.setId(inquiry.getId());
        response.setTitle(inquiry.getTitle());
        response.setContent(inquiry.getContent());
        response.setStatus(inquiry.getStatus());
        response.setCreatedAt(inquiry.getCreatedAt());
        response.setUpdatedAt(inquiry.getUpdatedAt());

       // UserResponse userResponse = new UserResponse();
        //userResponse.setId(inquiry.getUser().getId());
       // userResponse.setName(inquiry.getUser().getName());
       // response.setUser(userResponse);

        return response;
    }

    private User getCurrentUser() {
        Long userId = 1L;
        return userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User not found with ID : "+userId));
    }
}
