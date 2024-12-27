package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.ConsultationAnswer;

@Repository
public interface ConsultationAnswerRepository extends JpaRepository<ConsultationAnswer, Long> {
}
