package com.goorm.devsync.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiriesRepository extends JpaRepository<Inquiries, Long> {
    Page<Inquiries> findByStatus(Status status,Pageable pageable);
}
