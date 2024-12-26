package com.example.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.entity.Inquiry;
import com.example.project.service.InquiryService;

@Controller
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/center/email/save")
    public String saveInquiry(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String content) {
        Inquiry inquiry = new Inquiry();
        inquiry.setName(name);
        inquiry.setEmail(email);
        inquiry.setContent(content);

        inquiryService.save(inquiry); // 저장 로직
        return "redirect:/center/email"; // 저장 후 게시판으로 리다이렉트
    }

    @GetMapping("/center/email/modify")
    public String ModifyForm(@RequestParam Long id, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "modify";
    }

    @PostMapping("/center/email/update")
    public String updateInquiry(Inquiry inquiry) {
        inquiryService.update(inquiry);
        return "redirect:/center/email";
    }
}
