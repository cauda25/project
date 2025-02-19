package com.example.project.repository.reserve;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.project.entity.reserve.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    @Query("SELECT t.theaterState FROM Theater t GROUP BY t.theaterState ORDER BY MIN(t.theaterId) ASC")
    List<String> findAllRegions();

    List<Theater> findByTheaterStateOrderByTheaterIdAsc(String theaterState);

}
