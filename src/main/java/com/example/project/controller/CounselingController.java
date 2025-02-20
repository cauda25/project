package com.example.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.project.entity.Member;
import com.example.project.service.InquiryService;
import com.example.project.service.MemberService;

@Controller
@RequestMapping("/center/counseling")
public class CounselingController {

    private final InquiryService inquiryService;
    private final MemberService memberService;

    // 생성자 주입 사용
    public CounselingController(InquiryService inquiryService, MemberService memberService) {
        this.inquiryService = inquiryService;
        this.memberService = memberService;
    }

    // 상담 제출 (회원만 작성 가능)
    @PostMapping("/submit")
    public String submitCounseling(@RequestParam String content, Principal principal, Model model) {
        if (principal == null) {
            model.addAttribute("error", "로그인이 필요한 서비스입니다.");
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }

        String username = principal.getName(); // 로그인된 사용자의 username을 가져옴

        // 현재 회원을 찾기 위해 SecurityContext에서 username으로 Member 객체를 가져옴
        Member member = memberService.findByUsername(username);

        // Inquiry 객체를 생성하여 상담 내용 저장
        Inquiry inquiry = new Inquiry();
        inquiry.setMember(member); // 로그인한 회원 정보 설정
        inquiry.setContent(content); // 상담 내용 설정

        // Inquiry 객체를 저장
        inquiryService.saveCounseling(inquiry); // InquiryService에서 상담 저장

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

        String currentUsername = authentication.getName(); // 로그인한 사용자 확인
        System.out.println("현재 로그인한 사용자: " + currentUsername); // 디버깅

        List<Inquiry> inquiries = inquiryService.getInquiriesByCurrentMember();
        model.addAttribute("inquiries", inquiries);
        return "center/list"; // 상담 목록 페이지로 이동
    }

    // 답변된 상담 목록
    @GetMapping("/answered")
    public String showAnsweredInquiries(Model model) {
        List<Inquiry> answeredInquiries = inquiryService.getInquiriesByStatus("answered");
        model.addAttribute("inquiries", answeredInquiries);
        return "center/answered"; // 답변된 상담 목록 페이지로 이동
    }

    // 미답변된 상담 목록
    @GetMapping("/unanswered")
    public String showUnansweredInquiries(Model model) {
        List<Inquiry> unansweredInquiries = inquiryService.getInquiriesByStatus("unanswered");
        model.addAttribute("inquiries", unansweredInquiries);
        return "center/unanswered"; // 미답변된 상담 목록 페이지로 이동
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
