package com.example.project.repository.reserve;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.entity.reserve.Screening;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

        // 극장에 상영중인 영화
        List<Screening> findByAuditorium_Theater_TheaterIdOrderByScreeningIdAsc(Long theaterId);

        // 영화별 상영시간표
        List<Screening> findByMovieTitleAndScreeningDate(String movieTitle, LocalDate screeningDate);

        @Modifying
        @Transactional
        @Query("UPDATE Screening s SET s.movieTitle = :newTitle WHERE s.auditorium.auditoriumNo = :auditoriumNo")
        int updateMovieTitleByAuditorium(@Param("auditoriumNo") Long auditoriumNo, @Param("newTitle") String newTitle);

        @Modifying
        @Transactional
        @Query("DELETE FROM Screening s WHERE s.screeningDate < :date")
        int deletePastScreenings(@Param("date") LocalDate date);

        long countByScreeningDate(LocalDate screeningDate);

        @Modifying
        @Transactional
        @Query("INSERT INTO Screening (startTime, movieTitle, runtime, screeningDate, auditorium) " +
                        "SELECT s.startTime, s.movieTitle, s.runtime, :targetDate, s.auditorium FROM Screening s " +
                        "WHERE s.screeningDate = :earliestDate")
        int copyTodayScreeningsToDate(@Param("targetDate") LocalDate targetDate,
                        @Param("earliestDate") LocalDate earliestDate);

        List<Screening> findAllByScreeningDate(LocalDate screeningDate);

        @Query("SELECT MIN(s.screeningDate) FROM Screening s")
        LocalDate findEarliestScreeningDate();

}
