package com.example.project.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Inquiry;
import com.example.project.entity.Member;
import com.example.project.service.InquiryService;
import com.example.project.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/center/inquiry")
public class InquiryController {

    @Autowired
    private final InquiryService inquiryService;

    @Autowired
    private final MemberService memberService;

    public InquiryController(InquiryService inquiryService, MemberService memberService) {
        this.inquiryService = inquiryService;
        this.memberService = memberService;
    }

    // 문의 작성 폼 - 로그인한 사용자 정보 자동 입력
    @GetMapping("/email")
    public String emailForm(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        if (userDetails != null) {
            String memberId = userDetails.getUsername();
            System.out.println("로그인한 사용자 ID: " + memberId);

            MemberDto memberDto = memberService.getMemberById(memberId);

            if (memberDto != null) {
                session.setAttribute("userName", memberDto.getName());
                session.setAttribute("userEmail", memberDto.getEmail());
                session.setAttribute("userPhone", memberDto.getPhone());

                System.out.println("세션 저장 완료 : " + memberDto.getName() + ", " + memberDto.getEmail());
            }
        } else {
            // 로그인되지 않은 경우 기본 값 처리
            model.addAttribute("userName", "");
            model.addAttribute("userEmail", "");
            model.addAttribute("userPhone", "");
        }

        return "center/email"; // email.html 반환
    }

    // 문의 저장
    @PostMapping("/save")
    public String saveInquiry(@ModelAttribute Inquiry inquiry, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String memberId = userDetails.getUsername();
        MemberDto memberDto = memberService.getMemberById(memberId);

        if (memberDto == null) {
            return "redirect:/login"; // 회원 정보가 없으면 로그인 페이지로 리다이렉트
        }

        // inquiry에 사용자 정보 설정
        inquiry.setName(memberDto.getName());
        inquiry.setEmail(memberDto.getEmail());
        inquiry.setPhone(memberDto.getPhone());

        // 서비스에서 저장 처리
        inquiryService.saveInquiry(inquiry);

        return "redirect:/center/inquiry/my-inquiries"; // 저장 후 사용자 문의 내역 페이지로 이동
    }

    // 사용자 문의 내역 조회
    @GetMapping("/my-inquiries")
    public String getMyInquiries(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<Inquiry> inquiries = inquiryService.getInquiriesByUser();
        model.addAttribute("inquiries", inquiries);

        if (inquiries.isEmpty()) {
            model.addAttribute("message", "작성한 문의가 없습니다.");
        }

        return "center/counseling";
    }

    // 관리자 - 문의 목록 페이지
    @GetMapping("/list")
    public String listInquiries(Model model, @RequestParam(defaultValue = "1") int page) {
        Page<Inquiry> inquiries = inquiryService.getAllInquiries(PageRequest.of(page, 10));
        model.addAttribute("inquiries", inquiries.getContent());
        model.addAttribute("totalPages", inquiries.getTotalPages());
        model.addAttribute("currentPage", page);
        return "center/email/list"; // 이메일 목록 템플릿
    }

    // 관리자 - 답변 상태 변경
    @PostMapping("/{id}/status")
    public ResponseEntity<Inquiry> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Inquiry updatedInquiry = inquiryService.updateStatus(id, status);
            return ResponseEntity.ok(updatedInquiry);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 개별 문의 조회 (API)
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getInquiry(@PathVariable Long id) {
        try {
            Map<String, String> response = inquiryService.getInquiryDetails(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    // 문의 작성 폼 (별도 경로)
    @GetMapping("/inquiry")
    public String showInquiryForm(Model model, HttpSession session) {
        Inquiry inquiry = new Inquiry();
        Member member = (Member) session.getAttribute("member");

        // 로그인한 사용자가 있다면 username을 Inquiry에 설정
        if (member != null) {
            inquiry.setUsername(member.getUsername());
            inquiry.setEmail(member.getEmail());
            inquiry.setPhone(member.getPhone());
        }

        model.addAttribute("inquiry", inquiry);

        // 상담 페이지로 이동
        return "center/counseling";
    }

    // 로그인한 사용자의 문의 내역 조회
    @GetMapping
    public String getUserInquiries(@RequestParam(defaultValue = "1") int page, Model model) {
        Page<Inquiry> inquiries = inquiryService.getInquiriesByUser(page); // 페이징된 문의 내역 가져오기
        model.addAttribute("inquiries", inquiries.getContent()); // 문의 목록 전달
        model.addAttribute("currentPage", page); // 현재 페이지
        model.addAttribute("totalPages", inquiries.getTotalPages()); // 총 페이지 수
        return "inquiryList"; // 문의 목록 뷰
    }

    @PostMapping("/delete-selected")
    public String deleteSelectedInquiries(@RequestParam("inquiryIds") List<Long> inquiryIds,
            RedirectAttributes redirectAttributes) {
        try {
            for (Long id : inquiryIds) {
                inquiryService.deleteInquiry(id);
            }
            redirectAttributes.addFlashAttribute("successMessage", "문의가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/center/inquiry/my-inquiries";
    }
}
