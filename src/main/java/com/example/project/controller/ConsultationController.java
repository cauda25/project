package com.example.project.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.entity.Consultation;
import com.example.project.service.ConsultationService;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @GetMapping("/{userId}")
    public List<Consultation> getConsultationsByUserId(@PathVariable Long userId) {
        return consultationService.getConsultationsByUserId(userId);
    }

    @GetMapping
    public String getConsultations(Model model) {
        List<Consultation> consultations = consultationService.getAllConsultations();
        model.addAttribute("consultations", consultations);
        return "consultation/consultation"; // Thymeleaf 템플릿 경로
    }
}
