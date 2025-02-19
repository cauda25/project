package com.example.project.repository.reserve;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.entity.reserve.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    // 특정 날짜 범위에 있는 예약 건수 계산
    Long countByRegDateBetween(LocalDateTime start, LocalDateTime end);

    List<Reserve> findByMemberMid(Long mid);

    @Query("SELECT r FROM Reserve r JOIN FETCH r.seatStatuses ss JOIN FETCH ss.screening s WHERE r.member.mid = :mid")
    List<Reserve> findByMemberMidWithJoins(@Param("mid") Long mid);
}
