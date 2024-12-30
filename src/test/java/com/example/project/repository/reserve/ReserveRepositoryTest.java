package com.example.project.repository.reserve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.project.entity.Genre;
import com.example.project.entity.Movie;
import com.example.project.entity.MovieGenre;
import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.Auditorium;
import com.example.project.entity.reserve.Screening;
import com.example.project.entity.reserve.Seat;
import com.example.project.entity.reserve.SeatStatus;
import com.example.project.entity.reserve.Theater;
import com.example.project.repository.movie.MovieRepository;
import com.example.project.repository.reserve.AuditoriumRepository;
import com.example.project.repository.reserve.ScreeningRepository;
import com.example.project.repository.reserve.SeatStatusRepository;
import com.example.project.repository.reserve.TheaterRepository;
import com.example.project.service.MovieService;

@SpringBootTest
@Transactional
public class ReserveRepositoryTest {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private SeatStatusRepository seatStatusRepository;

    // @Test
    // public void testFindByScreeningId() {

    // // 데이터 삽입
    // Seat seat = new Seat();
    // seat.setRowNum("A");
    // seat.setSeatNum(1L);
    // // ... set other fields
    // seat = seatRepository.save(seat);

    // Screening screening = new Screening();
    // // ... set screening fields
    // screening = screeningRepository.save(screening);

    // SeatStatus seatStatus = new SeatStatus();
    // seatStatus.setSeat(seat);
    // seatStatus.setScreening(screening);
    // seatStatus.setSeatStatusEnum(SeatStatusEnum.AVAILABLE);
    // seatStatusRepository.save(seatStatus);

    // // 테스트
    // Long screeningId = screening.getScreeningId();
    // var result = seatStatusRepository.findByScreeningId(screeningId);
    // assertNotNull(result);
    // assertFalse(result.isEmpty());
    // }

}