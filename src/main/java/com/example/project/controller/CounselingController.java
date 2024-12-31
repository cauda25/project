package com.example.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.entity.Inquiry;
import com.example.project.service.InquiryService;

@Controller
@RequestMapping("/center/counseling")
public class CounselingController {

    private final InquiryService inquiryService;

    // 생성자 주입 사용
    public CounselingController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // 상담 제출 (회원만 작성 가능)
    @PostMapping("/submit")
    public String submitCounseling(@RequestParam String content, Principal principal, Model model) {
        if (principal == null) {
            model.addAttribute("error", "로그인이 필요한 서비스입니다.");
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }

        String username = principal.getName();
        // 로그인된 사용자 정보로 상담 내역 저장
        inquiryService.saveCounseling(username, content);
        return "redirect:/center/counseling/list"; // 상담 목록 페이지로 리디렉션
    }

    // 상담 목록 페이지 (회원만 접근 가능)
    @GetMapping("/list")
    public String showCounselingList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/member/login"; // 비로그인 상태일 경우 로그인 페이지로 리디렉트
        }

        List<Inquiry> inquiries = inquiryService.findAll();
        model.addAttribute("inquiries", inquiries);
        return "center/list"; // 상담 목록 페이지로 이동
    }

    // 상담 상세 페이지 (회원만 접근 가능)
    @GetMapping("/detail/{id}")
    public String getInquiryDetail(@PathVariable("id") Long inquiryId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/member/login"; // 비로그인 상태일 경우 로그인 페이지로 리디렉트
        }

        Inquiry inquiry = inquiryService.findInquiryById(inquiryId);
        model.addAttribute("inquiry", inquiry);
        return "center/detail"; // 상세 페이지로 이동
    }
}
