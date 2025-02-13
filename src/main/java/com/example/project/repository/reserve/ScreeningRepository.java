package com.example.project.repository.reserve;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.entity.reserve.Screening;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    // 극장에 상영중인 영화
    @Query("SELECT s FROM Screening s WHERE s.auditorium.theater.theaterId = :theaterId ORDER BY s.screeningId ASC")
    List<Screening> findScreeningsByTheaterId(@Param("theaterId") Long theaterId);

    // 영화별 상영시간표
    @Query(value = "SELECT * FROM Screening s WHERE s.movie_title = :movieTitle AND TRUNC(s.screening_date) = TRUNC(:date)", nativeQuery = true)
    List<Screening> findScreeningsByMovieAndDate(@Param("movieTitle") String movieTitle, @Param("date") LocalDate date);

    // Optional<Screening> findById(Long screeningId);

    @Modifying
    @Transactional
    @Query("UPDATE Screening s SET s.movieTitle = :newTitle WHERE s.auditorium.auditoriumNo = :auditoriumNo")
    int updateMovieTitleByAuditorium(@Param("auditoriumNo") Long auditoriumNo, @Param("newTitle") String newTitle);

    @Modifying
    @Transactional
    @Query("DELETE FROM Screening s WHERE s.screeningDate < :date")
    int deletePastScreenings(@Param("date") LocalDate date);

    @Query("SELECT COUNT(s) FROM Screening s WHERE s.screeningDate = :date")
    long countByScreeningDate(@Param("date") LocalDate date);

    @Modifying
    @Transactional
    @Query("INSERT INTO Screening (startTime, movieTitle, runtime, screeningDate, auditorium) " +
            "SELECT s.startTime, s.movieTitle, s.runtime, :targetDate, s.auditorium FROM Screening s " +
            "WHERE s.screeningDate = :todayDate")
    int copyTodayScreeningsToDate(@Param("targetDate") LocalDate targetDate,
            @Param("todayDate") LocalDate todayDate);

    List<Screening> findAllByScreeningDate(LocalDate screeningDate);
    // @Query(value = "INSERT INTO screening (start_time, movie_title, runtime,
    // screening_date, auditorium_no) " +
    // "SELECT start_time, movie_title, runtime, :targetDate, auditorium_no " +
    // "FROM screening WHERE screening_date = :todayDate", nativeQuery = true)

}
