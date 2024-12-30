package com.example.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.entity.Inquiry;
import com.example.project.service.InquiryService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/center")
public class CenterController {

    @Autowired
    private InquiryService inquiryService;

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
    public String getEmail(@RequestParam(defaultValue = "1") int page, Model model) {
        log.info(" 이메일게시판 요청");

        List<Inquiry> inquiries = inquiryService.getInquiries(page);
        model.addAttribute("inquiries", inquiries);
        model.addAttribute("totalPages", inquiryService.getTotalPages());
        model.addAttribute("currentPage", page);
        return "center/email";
    }

    @GetMapping("/counseling")
    public String getCounseling(Model model) {
        log.info("상담 내역 요청");

        List<Inquiry> inquiries = inquiryService.getAllInquiries();
        model.addAttribute("inquiries", inquiries);
        return "center/counseling";
    }

}
