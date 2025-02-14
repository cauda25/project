package com.example.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Inquiry;
import com.example.project.entity.Member;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 사용자의 사용자명(member)을 기준으로 여러 개의 Inquiry를 조회하는 메소드
    // 반환 타입은 List<Inquiry>로, 해당 사용자명과 관련된 모든 문의를 리스트로 반환
    List<Inquiry> findByMemberMid(Long mid);

    List<Inquiry> findByMember(Member member);

    Page<Inquiry> findByMember(Member member, Pageable pageable);

}
