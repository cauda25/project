package com.example.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.project.entity.Inquiry;
import com.example.project.entity.InquiryStatus;
import com.example.project.repository.InquiryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inquiry not found"));
    }

    public void saveInquiry(Inquiry inquiry) {
        inquiryRepository.save(inquiry);
    }

    public void deleteInquiry(Long id) {
        inquiryRepository.deleteById(id);
    }

    // 모든 문의 데이터 조회
    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAll();
    }

    public Inquiry updateStatus(Long id, String status) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));

        // String을 InquiryStatus로 변환
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

    // 새로운 문의 저장
    public void save(Inquiry inquiry) {
        inquiryRepository.save(inquiry);
    }

    // 페이지 나누기
    public List<Inquiry> getInquiries(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageable);
        return inquiryPage.getContent();
    }

    public int getTotalPages() {
        long count = inquiryRepository.count();
        return (int) Math.ceil((double) count / 10);
    }

    public void update(Inquiry inquiry) {
        Inquiry existingInquiry = inquiryRepository.findById(inquiry.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid inquiry ID: " + inquiry.getId()));

        existingInquiry.setName(inquiry.getName());
        existingInquiry.setEmail(inquiry.getEmail());
        existingInquiry.setContent(inquiry.getContent());

        inquiryRepository.save(existingInquiry);
    }

    public void delete(Long id) {
        if (!inquiryRepository.existsById(id)) {
            throw new IllegalArgumentException("유효하지 않는 문의 ID 입니다. " + id);
        }

        inquiryRepository.deleteById(id);
    }

    public Inquiry findById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found with id: " + id));
    }

    public List<Inquiry> findAll() {
        return inquiryRepository.findAll();
    }

    public Inquiry getInquiry(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inquiry not found"));
        if (inquiry.getAnswered() == null) {
            inquiry.setAnswered(false);
        }
        return inquiry;
    }

    // ID로 문의 조회 메서드 추가
    public Inquiry findInquiryById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found with id: " + id));
    }

    // 상담 저장
    public void saveCounseling(String username, String content) {
        Inquiry inquiry = new Inquiry();
        inquiry.setUsername(username);
        inquiry.setContent(content);
        inquiryRepository.save(inquiry);
    }

    // 사용자 이름(username)을 통해 이메일 조회
    public String getEmailByUsername(String username) {
        // 도메인 배열 생성
        String[] domains = { "@naver.com" };

        // 무작위로 도메인 선택
        int randomIndex = (int) (Math.random() * domains.length);
        String selectedDomain = domains[randomIndex];
        // 여기에 사용자 이메일 조회 로직 구현
        // 예시: username 기반으로 이메일 생성
        // 실제 구현은 데이터베이스나 사용자 관리 서비스와의 연동 필요
        return username + selectedDomain; // 예시로 도메인 추가
    }
}
