package com.example.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 사용자의 사용자명(username)을 기준으로 여러 개의 Inquiry를 조회하는 메소드
    // 반환 타입은 List<Inquiry>로, 해당 사용자명과 관련된 모든 문의를 리스트로 반환
    List<Inquiry> findByUser_Username(String username);

    // id를 기준으로 하나의 Inquiry를 조회하는 메소드
    // 반환 타입은 Optional<Inquiry>로, 해당 id에 해당하는 문의가 없을 수도 있기 때문에 Optional로 감싸서 반환
    Optional<Inquiry> findById(Long id);
}
