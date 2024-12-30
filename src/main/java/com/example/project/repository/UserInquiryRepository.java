package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.UserInquiry;

public interface UserInquiryRepository extends JpaRepository<UserInquiry, Long> {

}
