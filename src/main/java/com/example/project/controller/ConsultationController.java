package com.example.project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.entity.Consultation;
import com.example.project.service.ConsultationAnswerService;
import com.example.project.service.ConsultationService;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;
    private final ConsultationAnswerService consultationAnswerService;

    // 생성자 주입으로 두 개의 서비스 클래스 주입
    public ConsultationController(ConsultationService consultationService,
            ConsultationAnswerService consultationAnswerService) {
        this.consultationService = consultationService;
        this.consultationAnswerService = consultationAnswerService;
    }

    // 특정 사용자의 상담 목록 조회
    @GetMapping("/{userId}")
    public List<Consultation> getConsultationsByUserId(@PathVariable Long userId) {
        return consultationService.getConsultationsByUserId(userId);
    }

    // 상담 목록 조회 (관리자용)
    @GetMapping
    public String getConsultations(Model model) {
        List<Consultation> consultations = consultationService.getAllConsultations();
        model.addAttribute("consultations", consultations);
        return "consultation/consultation"; // Thymeleaf 템플릿 경로
    }

    // 상담에 대한 답변 추가
    @PostMapping("/answer/{consultationId}")
    public ResponseEntity<String> addAnswer(@PathVariable Long consultationId, @RequestBody String answerContent) {

        // 상담에 대한 답변을 추가하고 상태를 '답변완료'로 변경
        consultationAnswerService.addAnswer(consultationId, answerContent);
        return ResponseEntity.ok("답변이 추가되었습니다.");
    }
}
