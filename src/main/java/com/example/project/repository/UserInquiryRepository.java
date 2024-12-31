package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.User;

import com.example.project.entity.UserInquiry;

public interface UserInquiryRepository extends JpaRepository<UserInquiry, Long> {

    List<UserInquiry> findByUser(User user);
}
