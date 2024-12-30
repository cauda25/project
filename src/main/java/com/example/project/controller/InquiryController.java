package com.example.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    // 상태 업데이트
    @PostMapping("/{id}/status")
    public ResponseEntity<Inquiry> updateStatus(@PathVariable Long id, @RequestBody String status) {
        Inquiry updatedInquiry = inquiryService.updateStatus(id, status);
        return ResponseEntity.ok(updatedInquiry);
    }

    // 이메일 삭제
    @PostMapping("/center/email/delete")
    public String deleteInquiry(@RequestParam("id") Long id) {
        inquiryService.deleteInquiry(id); // 삭제 서비스 호출
        return "redirect:/center/email";
    }
}
