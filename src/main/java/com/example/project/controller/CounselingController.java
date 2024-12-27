package com.example.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.entity.Counseling;
import com.example.project.entity.Inquiry;
import com.example.project.repository.CounselingRepository;
import com.example.project.repository.InquiryRepository;

@Controller
public class CounselingController {

    @Autowired
    private CounselingRepository counselingRepository;

    @Autowired
    private InquiryRepository inquiryRepository;

    @GetMapping("/center/counseling/history")
    public String getCounselingHistory(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;

        // 총 페이지 수 계산
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Counseling> counselingsPage = counselingRepository.findAll(pageable);

        // 이메일 문의 데이터 조회
        List<Inquiry> inquiries = inquiryRepository.findAll();

        // 상담 내역에 이메일 문의 데이터를 통합
        List<Object> combinedList = new ArrayList<>(counselingsPage.getContent());
        combinedList.addAll(inquiries);

        // 모델에 데이터 추가
        model.addAttribute("counselings", combinedList);
        model.addAttribute("totalPages", counselingsPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "/center/counselingHistory";
    }

    @GetMapping("/list")
    public String showCounselingPage(Model model) {
        Inquiry counseling = new Inquiry();
        counseling.setCounselingType("Type A"); // set the counseling type
        model.addAttribute("counseling", counseling);

        return "center/counseling";
    }

}
