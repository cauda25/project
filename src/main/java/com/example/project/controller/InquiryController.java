package com.example.project.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.entity.Inquiry;
import com.example.project.service.InquiryService;

@Controller
@RequestMapping("/center/email")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // 글 작성 폼 (로그인된 사용자 정보 자동 입력)
    @GetMapping("/form")
    public String emailForm(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName(); // 로그인된 사용자 이름
            String email = inquiryService.getEmailByUsername(username); // 사용자 이메일 가져오기
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }
        return "email/form"; // 글 작성 폼 페이지
    }

    // 글 작성 저장
    @PostMapping("/save")
    public String saveInquiry(@RequestParam String content, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // 비로그인 상태일 경우 로그인 페이지로 리다이렉트
        }

        String username = principal.getName();
        String email = inquiryService.getEmailByUsername(username); // 사용자 이메일 가져오기

        Inquiry inquiry = new Inquiry();
        inquiry.setName(username);
        inquiry.setEmail(email);
        inquiry.setContent(content);

        inquiryService.save(inquiry); // 저장 로직
        return "redirect:/center/email"; // 저장 후 게시판으로 리다이렉트
    }

    @GetMapping("/modify")
    public String modifyForm(@RequestParam Long id, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "modify";
    }

    @PostMapping("/update")
    public String updateInquiry(Inquiry inquiry) {
        inquiryService.update(inquiry);
        return "redirect:/center/email";
    }

    // 상태 업데이트
    @PostMapping("/{id}/status")
    public ResponseEntity<Inquiry> updateStatus(@PathVariable Long id, @RequestBody String status) {
        Inquiry updatedInquiry = inquiryService.updateStatus(id, status);
        return ResponseEntity.ok(updatedInquiry);
    }

    // 이메일 삭제
    @PostMapping("/delete/{id}")
    public String deleteInquiry(@PathVariable Long id) {
        inquiryService.deleteInquiry(id); // 삭제 서비스 호출
        return "redirect:/center/email";
    }

    @GetMapping("/my-inquiries")
    public String getMyInquiries(Model model) {
        // 현재 로그인된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 인증되지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // 사용자 이름(username) 가져오기
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // 사용자 상담 내역 조회
        List<Inquiry> inquiries = inquiryService.getInquiriesByUsername(username);
        model.addAttribute("inquiries", inquiries);
        return "my-inquiries";
    }

    // 답변 및 미답변
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
