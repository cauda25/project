package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 사용자의 사용자명(username)을 기준으로 여러 개의 Inquiry를 조회하는 메소드
    // 반환 타입은 List<Inquiry>로, 해당 사용자명과 관련된 모든 문의를 리스트로 반환
    List<Inquiry> findByUsername(String username);

}
