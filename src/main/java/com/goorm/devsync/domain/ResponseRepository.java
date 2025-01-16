package com.goorm.devsync.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Responses, Long> {
    List<Responses> findByInquiryId(Long inquiryId);
}
