package com.example.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Inquiry;
import com.example.project.service.InquiryService;
import com.example.project.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/center")
public class CenterController {

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private MemberService memberService;

    @GetMapping("")
    public String getCenter() {
        log.info("center main 폼 요청");
        return "/center/home";
    }

    @GetMapping("/chat")
    public void getChat() {
        log.info(" ai 챗봇 요청");
    }

    @GetMapping("/email")
    public String getEmail(@RequestParam(defaultValue = "1") int page, Model model,
            @AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        log.info(" 이메일게시판 요청");

        Page<Inquiry> inquiryPage = inquiryService.getInquiries(page);
        List<Inquiry> inquiries = inquiryPage.getContent();

        model.addAttribute("inquiries", inquiries);
        model.addAttribute("totalPages", inquiryPage.getTotalPages());
        model.addAttribute("currentPage", page);

        if (userDetails != null) {
            System.out.println("test2");
            String memberId = userDetails.getUsername();
            System.out.println("로그인한 사용자 ID: " + memberId);

            MemberDto memberDto = memberService.getMemberById(memberId);

            if (memberDto != null) {
                session.setAttribute("userName", memberDto.getName());
                session.setAttribute("userEmail", memberDto.getEmail());
                session.setAttribute("userPhone", memberDto.getPhone());

                System.out.println("세션 저장 완료 : " + memberDto.getName() + ", " + memberDto.getEmail());

                // 세션에서 값을 모델에 추가
                model.addAttribute("userName", memberDto.getName());
                model.addAttribute("userEmail", memberDto.getEmail());
                model.addAttribute("userPhone", memberDto.getPhone());
            }
        } else {
            // 로그인되지 않은 경우 기본 값 처리
            model.addAttribute("userName", "");
            model.addAttribute("userEmail", "");
            model.addAttribute("userPhone", "");
        }

        return "center/email";
    }

    @GetMapping("/counseling")
    public String getCounseling(Model model) {
        log.info("상담 내역 요청");

        List<Inquiry> inquiries = inquiryService.getAllInquiries(1);

        model.addAttribute("inquiries", inquiries);
        return "center/counseling";
    }

    // 사용자 문의 내역 조회
    @GetMapping("/my-inquiries")
    public String getMyInquiries(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("사용자 문의 내역 요청");
        if (userDetails == null) {
            return "redirect:/login";
        }

        String memberId = userDetails.getUsername(); // 현재 로그인된 사용자의 ID 가져오기

        // memberId가 비어 있거나 null이면 로그인 페이지로 리디렉트
        if (memberId == null || memberId.isEmpty()) {
            return "redirect:/login";
        }

        // 해당 사용자의 문의 목록 가져오기
        List<Inquiry> inquiries = inquiryService.getInquiriesByUser(memberId);

        // 모델에 데이터 추가
        model.addAttribute("inquiries", inquiries);
        model.addAttribute("message", inquiries.isEmpty() ? "작성한 문의가 없습니다." : "");

        return "center/counseling";
    }

}
