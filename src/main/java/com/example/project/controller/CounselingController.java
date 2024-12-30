package com.example.project.controller;

import java.security.Principal;
import java.util.List;

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

    // 상담 제출
    @PostMapping("/submit")
    public String submitCounseling(@RequestParam String content, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // 로그인하지 않은 경우
        }

        String username = principal.getName();
        // 로그인된 사용자 정보로 상담 내역 저장
        inquiryService.saveCounseling(username, content);
        return "redirect:/center/counseling/list"; // 상담 목록 페이지로 리디렉션
    }

    // 상담 목록 페이지
    @GetMapping("/list")
    public String showCounselingList(Model model) {
        List<Inquiry> inquiries = inquiryService.findAll();
        model.addAttribute("inquiries", inquiries);
        return "center/list"; // 상담 목록 페이지로 이동
    }

    // 상담 상세 페이지
    @GetMapping("/detail/{id}")
    public String getInquiryDetail(@PathVariable("id") Long inquiryid, Model model) {
        Inquiry inquiry = inquiryService.findInquiryById(inquiryid);
        model.addAttribute("inquiry", inquiry);
        return "center/detail"; // 상세 페이지로 이동
    }
}
