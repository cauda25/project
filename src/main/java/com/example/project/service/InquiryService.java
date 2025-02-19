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

import com.example.project.dto.InquiryDto;
import com.example.project.dto.MemberDto;
import com.example.project.entity.Inquiry;
import com.example.project.entity.InquiryStatus;
import com.example.project.entity.Member;
import com.example.project.repository.InquiryRepository;
import com.example.project.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private MemberRepository memberRepository;

    private InquiryService inquiryService;

    // ID로 문의 조회
    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
    }

    // 문의 저장
    public void saveInquiry(Inquiry inquiry) {
        System.out.println("저장 전 상태: " + inquiry.getStatus()); // 로그 추가
        inquiryRepository.save(inquiry);
        System.out.println("저장할 문의 ID: " + inquiry.getId() + ", 상태: " + inquiry.getStatus());
    }

    // ID로 문의 삭제
    @Transactional
    public void deleteInquiriesByIds(List<Long> inquiryIds) {
        if (inquiryIds == null || inquiryIds.isEmpty()) {
            throw new IllegalArgumentException("삭제할 문의 ID 목록이 비어 있습니다.");
        }

        List<Inquiry> inquiries = inquiryRepository.findAllById(inquiryIds);
        if (inquiries.isEmpty()) {
            throw new EntityNotFoundException("해당 ID에 해당하는 문의가 없습니다.");
        }

        inquiryRepository.deleteAll(inquiries);
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

    public Page<Inquiry> getAllInquiries(Pageable pageable) {
        Page<Inquiry> inquiries = inquiryRepository.findAll(pageable);

        // 조회된 문의 상태 출력 (디버깅용)
        inquiries.forEach(
                inquiry -> System.out.println("조회된 문의 ID: " + inquiry.getId() + ", 상태: " + inquiry.getStatus()));

        return inquiries;
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

    // 예시: setStatus와 saveCounseling을 수정한 코드
    public void updateInquiryStatus(Long inquiryId, String status) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        // setStatus 수정: String을 InquiryStatus로 변환하여 사용
        inquiry.setStatus(InquiryStatus.valueOf(status)); // "answered" 또는 "unanswered"

        // saveCounseling 수정: Member와 String을 전달
        inquiryService.saveCounseling(inquiry.getMember(), inquiry.getContent()); // inquiry.getMember()와
                                                                                  // inquiry.getContent() 전달

        // 저장
        inquiryRepository.save(inquiry);
    }

    // Inquiry 객체를 저장하는 메소드
    public Inquiry save(Inquiry inquiry) {
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

        String username = authentication.getName(); // 현재 로그인한 사용자의 memberId 가져오기
        System.out.println("로그인한 사용자명(username) : " + username);

        return memberRepository.findByMemberId(username)
                .orElseThrow(() -> {
                    System.out.println("ERROR : 해당 ID(" + username + ")을 가진 회원을 찾을 수 없음");
                    return new RuntimeException("Member not found with memberid" + username);
                });
    }

    // memberId로 Member 조회
    public InquiryDto getMemberInfoForInquiry(String memberId) {
        try {
            // memberId를 Long으로 변환 후 조회
            Long id = Long.parseLong(memberId);
            Optional<Member> memberOptional = memberRepository.findById(id);

            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();

                // InquiryDto로 변환하여 반환
                return InquiryDto.builder()
                        .name(member.getName())
                        .email(member.getEmail())
                        .phone(member.getPhone())
                        .username(member.getMemberId()) // username 필드에 회원 ID 저장
                        .build();
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("memberId 변환 실패: " + memberId);
            return null;
        }
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

    // 로그인한 사용자의 문의 내역 조회 (페이징 포함)
    public Page<Inquiry> getInquiriesByUsername(String username, int page) {

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Inquiry> inquiries = inquiryRepository.findByUsername(username, pageable);

        inquiries.forEach(inquiry -> {
            if (inquiry.getMember() != null) {
                System.out.println("Inquiry ID: " + inquiry.getId() + " | Member: " + inquiry.getMember().getName());
            } else {
                System.out.println("Inquiry ID: " + inquiry.getId() + " | Member: NULL");
            }
        });

        return inquiryRepository.findByUsername(username, pageable);
    }

    // 기존의 memberId만 받는 메서드 (페이징 없이 사용 가능)
    public List<Inquiry> getInquiriesByUser(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID(" + memberId + ")를 가진 회원을 찾을 수 없습니다."));

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

    // 답변을 처리하고 상태를 ANSWERED로 변경하는 서비스 메서드
    public void answerInquiry(Long inquiryId, String answer) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inquiry Id"));

        // 답변 내용 설정
        inquiry.setAnswer(answer);

        // 상태 변경
        inquiry.setStatus(InquiryStatus.ANSWERED); // 상태를 ANSWERED로 변경

        // 변경된 상태 저장
        inquiryRepository.save(inquiry);
    }

    // 상담 내역
    public List<Inquiry> findInquiriesByMember(Member member) {
        return inquiryRepository.findByMember(member); // Member와 연결된 상담 내역 조회
    }

    // 상태에 따른 상담 목록 조회
    public List<Inquiry> getInquiriesByStatus(String status) {
        return inquiryRepository.findByStatus(status); // 상태에 따라 조회
    }

    public Inquiry findInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId).orElse(null); // 상담 내역 조회
    }

    // 로그인한 사용자의 상담 내역 조회 (본인만 조회 가능)
    public List<Inquiry> getInquiriesByCurrentMember() {
        Member currentMember = getCurrentMember(); // 현재 로그인된 사용자의 정보 가져오기
        return inquiryRepository.findByMember(currentMember); // 본인의 상담 내역만 조회
    }
}
