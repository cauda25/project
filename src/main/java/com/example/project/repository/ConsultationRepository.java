package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByUserId(Long userId);
}
