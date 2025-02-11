package com.example.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.project.entity.Inquiry;
import com.example.project.entity.InquiryStatus;
import com.example.project.entity.User;
import com.example.project.repository.InquiryRepository;
import com.example.project.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private UserRepository userRepository;

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

    // 모든 문의 조회 (페이징 X)
    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAll();
    }

    // 페이징 처리된 문의 조회 (수정된 부분)
    public Page<Inquiry> getAllInquiries(Pageable pageable) {
        return inquiryRepository.findAll(pageable);
    }

    // 문의 상태 업데이트
    public Inquiry updateStatus(Long id, String status) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));

        try {
            InquiryStatus inquiryStatus = InquiryStatus.valueOf(status);
            inquiry.setStatus(inquiryStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 상태 값입니다: " + status);
        }

        return inquiryRepository.save(inquiry);
    }

    // 생성자 주입
    public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    // 페이지별 문의 조회
    public List<Inquiry> getInquiries(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageable);
        return inquiryPage.getContent();
    }

    // 총 페이지 수 계산
    public int getTotalPages() {
        long count = inquiryRepository.count();
        return (int) Math.ceil((double) count / 10);
    }

    // 기존 문의 수정
    public void update(Inquiry inquiry) {
        Inquiry existingInquiry = inquiryRepository.findById(inquiry.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid inquiry ID: " + inquiry.getId()));

        existingInquiry.setName(inquiry.getName());
        existingInquiry.setEmail(inquiry.getEmail());
        existingInquiry.setContent(inquiry.getContent());

        inquiryRepository.save(existingInquiry);
    }

    // ID로 문의 조회 및 검증
    public Inquiry getInquiry(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inquiry not found"));
        if (inquiry.getAnswered() == null) {
            inquiry.setAnswered(false);
        }
        return inquiry;
    }

    // 사용자 이름(username)으로 문의 조회
    public List<Inquiry> getInquiriesByUsername(String username) {
        return inquiryRepository.findByUser_Username(username);
    }

    // 사용자 이메일 조회 (더미 도메인 추가)
    public String getEmailByUsername(String username) {
        String[] domains = { "@naver.com" };
        int randomIndex = (int) (Math.random() * domains.length);
        String selectedDomain = domains[randomIndex];

        return username + selectedDomain;
    }

    public String getMobileByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return (user != null) ? user.getMobile() : null;
    }

    // 상담 내용 저장 (수정된 부분)
    public void saveCounseling(String username, String email, String content) {
        Inquiry inquiry = new Inquiry(username, email, content);
        inquiryRepository.save(inquiry);
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
