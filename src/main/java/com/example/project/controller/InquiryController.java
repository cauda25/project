package com.example.project.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.project.entity.Inquiry;
import com.example.project.service.InquiryService;

@Controller
@RequestMapping("/center/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // 문의 작성 폼 - 로그인한 사용자 정보 자동 입력
    @GetMapping("/new")
    public String emailForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();

            // 서비스에서 사용자 정보를 조회
            String email = inquiryService.getEmailByUsername(username);
            String mobile = inquiryService.getMobileByUsername(username);
            String name = inquiryService.getNameByUsername(username);

            // 뷰에 데이터 전달
            model.addAttribute("userName", name);
            model.addAttribute("userEmail", email);
            model.addAttribute("userMobile", mobile);
        } else {
            // 로그인되지 않은 경우 기본 값 처리
            model.addAttribute("userName", "");
            model.addAttribute("userEmail", "");
            model.addAttribute("userMobile", "");
        }

        return "email"; // email.html 반환
    }

    // 문의 저장
    @PostMapping("/save")
    public String saveInquiry(@ModelAttribute Inquiry inquiry, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String username = userDetails.getUsername();

        // Inquiry 객체에 로그인 사용자 정보 추가
        inquiry.setUsername(username);
        inquiry.setEmail(inquiryService.getEmailByUsername(username));
        inquiry.setMobile(inquiryService.getMobileByUsername(username));

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

        String username = userDetails.getUsername();
        List<Inquiry> inquiries = inquiryService.getInquiriesByUsername(username);
        model.addAttribute("inquiries", inquiries);
        return "email/my-inquiries"; // 사용자 문의 내역 템플릿
    }

    // 관리자 - 문의 목록 페이지
    @GetMapping
    public String listInquiries(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Inquiry> inquiries = inquiryService.getAllInquiries(PageRequest.of(page, 10));
        model.addAttribute("inquiries", inquiries.getContent());
        model.addAttribute("totalPages", inquiries.getTotalPages());
        model.addAttribute("currentPage", page);
        return "email/list"; // 이메일 목록 템플릿
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

    @GetMapping("/center/email/form")
    public String showInquiryForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        return "inquiryForm"; // inquiryForm.html로 이동
    }

}
