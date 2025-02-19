package com.example.project.repository.reserve;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.SeatStatus;

public interface SeatStatusRepository extends JpaRepository<SeatStatus, Long> {

    List<SeatStatus> findByScreening_ScreeningId(Long screeningId);

    @Query("SELECT COUNT(s) FROM SeatStatus s WHERE s.screening.screeningId = :screeningId AND s.seatStatusEnum = 'AVAILABLE'")
    int countAvailableSeats(@Param("screeningId") Long screeningId);

    int countByScreening_ScreeningId(Long screeningId);

    @Query("SELECT ss FROM SeatStatus ss JOIN FETCH ss.seat s WHERE ss.screening.screeningId = :screeningId")
    List<SeatStatus> findSeatStatusesByScreeningId(@Param("screeningId") Long screeningId);

    List<SeatStatus> findBySeatStatusEnumAndUpdateDateBefore(SeatStatusEnum status, LocalDateTime time);

    @Modifying
    @Transactional
    int deleteByScreening_ScreeningDateBefore(LocalDate date);

}
