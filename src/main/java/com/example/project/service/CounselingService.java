package com.example.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.project.entity.Counseling;
import com.example.project.repository.CounselingRepository;

@Service
public class CounselingService {

    private final CounselingRepository counselingRepository;

    public CounselingService(CounselingRepository counselingRepository) {
        this.counselingRepository = counselingRepository;
    }

    // 모든 상담 내역 가져오기
    public List<Counseling> findAll() {
        return counselingRepository.findAll();
    }

    // 특정 ID로 상담 내역 찾기
    public Counseling findById(Long id) {
        Optional<Counseling> counseling = counselingRepository.findById(id);
        return counseling.orElseThrow(() -> new IllegalArgumentException("Invalid counseling ID: " + id));
    }

    // 상담 내역 저장
    public void save(Counseling counseling) {
        counselingRepository.save(counseling);
    }

    // 상담 내역 삭제
    public void deleteById(Long id) {
        counselingRepository.deleteById(id);
    }

}
