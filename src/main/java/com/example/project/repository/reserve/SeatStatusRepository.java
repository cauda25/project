package com.example.project.repository.reserve;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.SeatStatus;

public interface SeatStatusRepository extends JpaRepository<SeatStatus, Long> {
    @Query("SELECT ss FROM SeatStatus ss WHERE ss.screening.screeningId = :screeningId")
    List<SeatStatus> findByScreeningId(@Param("screeningId") Long screeningId);

    @Query("SELECT COUNT(s) FROM SeatStatus s WHERE s.screening.screeningId = :screeningId AND s.seatStatusEnum = 'AVAILABLE'")
    int countAvailableSeats(@Param("screeningId") Long screeningId);

    @Query("SELECT COUNT(s) FROM SeatStatus s WHERE s.screening.screeningId = :screeningId")
    int countTotalSeats(@Param("screeningId") Long screeningId);
}
