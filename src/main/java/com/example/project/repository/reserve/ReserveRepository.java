package com.example.project.repository.reserve;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.reserve.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    // 특정 날짜 범위에 있는 예약 건수 계산
    Long countByRegDateBetween(LocalDateTime start, LocalDateTime end);
}
