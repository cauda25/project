package com.example.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Inquiry;
import com.example.project.entity.InquiryStatus;
import com.example.project.entity.Member;
import com.example.project.repository.InquiryRepository;
import com.example.project.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private MemberRepository memberRepository;

    // ID로 문의 조회
    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
    }

    // 문의 저장
    public void saveInquiry(Inquiry inquiry) {
        inquiryRepository.save(inquiry);
    }

    // ID로 문의 삭제
    public void deleteInquiry(Long id) {
        if (!inquiryRepository.existsById(id)) {
            throw new IllegalArgumentException("유효하지 않는 문의 ID 입니다. " + id);
        }
        inquiryRepository.deleteById(id);
    }

    // 페이징 처리된 문의 조회 (Page<Inquiry> 반환)
    public Page<Inquiry> getInquiries(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10); // 한 페이지당 10개씩
        return inquiryRepository.findAll(pageable);
    }

    // 총 페이지 수 반환
    public int getTotalPages() {
        return getInquiries(1).getTotalPages(); // 총 페이지 수 반환
    }

    // 페이징 처리된 전체 문의 조회 (Page<Inquiry> 반환)
    public List<Inquiry> getAllInquiries(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageable);
        return inquiryPage.getContent();
    }

    // 문의 상태 업데이트
    public Inquiry updateStatus(Long id, String status) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));

        try {
            InquiryStatus inquiryStatus = InquiryStatus.valueOf(status.toUpperCase());
            inquiry.setStatus(inquiryStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 상태 값입니다: " + status);
        }

        return inquiryRepository.save(inquiry);
    }

    // memberDto에서 id를 직접 접근하는 방법
    public List<Inquiry> getInquiriesByMember(MemberDto memberDto) {
        // MemberDto를 이용하여 Member 조회
        Member member = memberRepository.findById(memberDto.getMid()) // MemberDto에서 id를 찾아 Member 조회
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return inquiryRepository.findByMember(member); // Member 객체를 이용한 조회
    }

    // 현재 로그인한 사용자 정보 가져오기
    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }
        String memberId = authentication.getName(); // 현재 로그인한 사용자의 memberId 가져오기
        return getMemberById(memberId);
    }

    // memberId로 Member 조회
    public Member getMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    // 현재 로그인한 사용자의 이메일 조회
    public String getEmailByCurrentUser() {
        return getCurrentMember().getEmail();
    }

    // 현재 로그인한 사용자의 휴대전화 조회
    public String getMobileByCurrentUser() {
        return getCurrentMember().getPhone();
    }

    // 현재 로그인한 사용자의 이름 조회
    public String getNameByCurrentUser() {
        return getCurrentMember().getName();
    }

    // 상담 내용 저장 (member 기반)
    public void saveCounseling(Member member, String content) {
        Inquiry inquiry = new Inquiry();
        inquiry.setMember(member);
        inquiry.setEmail(member.getEmail());
        inquiry.setContent(content);
        inquiryRepository.save(inquiry);
    }

    // 로그인한 사용자의 문의 내역 조회 추가
    public List<Inquiry> getInquiriesByUser() {
        Member member = getCurrentMember();
        return inquiryRepository.findByMember(member);
    }

    // 문의 답변 여부 확인
    public Map<String, String> getInquiryDetails(Long id) {
        Optional<Inquiry> inquiryOptional = inquiryRepository.findById(id);
        Map<String, String> response = new HashMap<>();

        if (inquiryOptional.isPresent()) {
            Inquiry inquiry = inquiryOptional.get();
            if (inquiry.getAnswer() != null && !inquiry.getAnswer().isEmpty()) {
                response.put("status", "답변");
                response.put("answer", inquiry.getAnswer());
            } else {
                response.put("status", "미답변");
                response.put("answer", null);
            }
        } else {
            throw new RuntimeException("해당 ID의 상담이 존재하지 않습니다.");
        }

        return response;
    }
}
