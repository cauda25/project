package com.example.project.repository.reserve;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.entity.reserve.Screening;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    // 극장에 상영중인 영화
    @Query("SELECT s FROM Screening s WHERE s.auditorium.theater.theaterId = :theaterId")
    List<Screening> findScreeningsByTheaterId(@Param("theaterId") Long theaterId);

    // 영화별 상영시간표
    @Query(value = "SELECT * FROM Screening s WHERE s.movie_title = :movieTitle AND TRUNC(s.screening_date) = TRUNC(:date)", nativeQuery = true)
    List<Screening> findScreeningsByMovieAndDate(@Param("movieTitle") String movieTitle, @Param("date") LocalDate date);

}
