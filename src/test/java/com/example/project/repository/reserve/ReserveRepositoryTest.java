package com.example.project.repository.reserve;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.Screening;
import com.example.project.entity.reserve.Seat;
import com.example.project.entity.reserve.SeatStatus;

@SpringBootTest
public class ReserveRepositoryTest {
    @Autowired
    private ReserveRepository reserveRepository;
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private SeatStatusRepository seatStatusRepository;
    @Autowired
    private SeatRepository seatRepository;

    // @Test
    // public void insertSeatStatusByScreeningRange() {
    // SeatStatusEnum defaultStatus = SeatStatusEnum.AVAILABLE;

    // IntStream.rangeClosed(100, 110).forEach(screeningId -> {
    // System.out.println("Processing Screening ID: " + screeningId);

    // Optional<Screening> optionalScreening = screeningRepository.findById((long)
    // screeningId);
    // if (optionalScreening.isPresent()) {
    // Screening screening = optionalScreening.get();
    // System.out.println("Found Screening: " + screening);

    // Long auditoriumId = screening.getAuditorium().getAuditoriumNo();
    // System.out.println("Auditorium ID: " + auditoriumId);

    // List<Seat> seats =
    // seatRepository.findByAuditorium_AuditoriumNo(auditoriumId);
    // System.out.println("Found Seats: " + seats.size());

    // seats.forEach(seat -> {
    // SeatStatus seatStatus = new SeatStatus();
    // seatStatus.setSeat(seat);
    // seatStatus.setScreening(screening);
    // seatStatus.setSeatStatusEnum(defaultStatus);

    // seatStatusRepository.save(seatStatus);
    // });

    // System.out.println("Inserted SeatStatus for Screening ID: " + screeningId);
    // } else {
    // System.out.println("Screening ID not found: " + screeningId);
    // }
    // });
    // }
}
