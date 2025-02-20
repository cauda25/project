package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.User;

import com.example.project.entity.UserInquiry;

public interface UserInquiryRepository extends JpaRepository<UserInquiry, Long> {

    // 특정 사용자가 작성한 모든 문의를 조회하는 메소드
    // 반환 타입은 List<UserInquiry>로, 해당 사용자와 관련된 모든 문의를 리스트로 반환
    List<UserInquiry> findByUser(User user);
}
