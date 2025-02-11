package com.example.project.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.project.entity.Inquiry;
import com.example.project.service.InquiryService;

@Controller
@RequestMapping("/center/inquiries") // URL을 /center/inquiries로 변경
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // 문의 목록 페이지
    @GetMapping
    public String listInquiries(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Inquiry> inquiries = inquiryService.getAllInquiries(PageRequest.of(page, 10)); // 페이징 추가
        model.addAttribute("inquiries", inquiries.getContent());
        model.addAttribute("totalPages", inquiries.getTotalPages());
        model.addAttribute("currentPage", page);
        return "email/list";
    }

    // 글 작성 폼 (로그인한 사용자 정보 자동 입력)
    @GetMapping("/form")
    public String emailForm(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            String email = inquiryService.getEmailByUsername(username);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }
        return "email/form";
    }

    // 문의 저장
    @PostMapping("/save")
    public String saveInquiry(@RequestParam String content, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        String email = inquiryService.getEmailByUsername(username);
        String mobile = inquiryService.getMobileByUsername(username);

        Inquiry inquiry = new Inquiry(username, email, content);
        inquiryService.saveInquiry(inquiry);

        return "redirect:/center/counseling"; // URL 수정
    }

    // 문의 수정 폼
    @GetMapping("/modify")
    public String modifyForm(@RequestParam Long id, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "email/modify";
    }

    // 문의 업데이트
    @PostMapping("/update")
    public String updateInquiry(Inquiry inquiry) {
        inquiryService.update(inquiry);
        return "redirect:/center/inquiries"; // URL 수정
    }

    // 문의 삭제
    @PostMapping("/delete/{id}")
    public String deleteInquiry(@PathVariable Long id) {
        inquiryService.deleteInquiry(id);
        return "redirect:/center/inquiries"; // URL 수정
    }

    // 사용자 문의 내역 조회
    @GetMapping("/my-inquiries")
    public String getMyInquiries(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        Object principal = authentication.getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername()
                : principal.toString();

        List<Inquiry> inquiries = inquiryService.getInquiriesByUsername(username);
        model.addAttribute("inquiries", inquiries);
        return "email/my-inquiries";
    }

    // 관리자 - 답변 상태 변경
    @PostMapping("/{id}/status")
    public ResponseEntity<Inquiry> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Inquiry updatedInquiry = inquiryService.updateStatus(id, status);
        return ResponseEntity.ok(updatedInquiry);
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
}
