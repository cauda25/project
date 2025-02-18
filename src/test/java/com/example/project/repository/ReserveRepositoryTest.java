package com.example.project.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.Screening;
import com.example.project.entity.reserve.Seat;
import com.example.project.entity.reserve.SeatStatus;
import com.example.project.repository.reserve.ReserveRepository;
import com.example.project.repository.reserve.ScreeningRepository;
import com.example.project.repository.reserve.SeatRepository;
import com.example.project.repository.reserve.SeatStatusRepository;

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

    @Transactional
    @Test
    @Rollback(false)
    public void selectTest() {
        // System.out.println("예매내역" + reserveRepository.findById(1L));
        // System.out.println(LocalDate.now());

    }

    @Test
    public void testUpdateMovieTitleByAuditorium() {

        Long auditoriumNo = 5L;
        String newMovieTitle = "엘리: 몬스터 패밀리";

        int updatedCount = screeningRepository.updateMovieTitleByAuditorium(auditoriumNo, newMovieTitle);

        assertTrue(updatedCount > 0, "업데이트된 행 개수가 1 이상이어야 합니다.");

    }

    // 상영시간과 좌석정보 업데이트
    @Test
    @Transactional
    @Rollback(false)
    public void testManageScreenings() {
        LocalDate today = LocalDate.now();

        // 오늘 이전 Screening과 관련된 SeatStatus 삭제 후 Screening 삭제
        int deletedSeatStatuses = seatStatusRepository.deleteByScreening_ScreeningDateBefore(today);
        int deletedScreenings = screeningRepository.deleteByScreeningDateBefore(today);
        System.out.println("지난 SeatStatus 삭제 완료: " + deletedSeatStatuses + "개");
        System.out.println("지난 Screening 삭제 완료: " + deletedScreenings + "개");

        LocalDate earliestDate = screeningRepository.findEarliestScreeningDate();

        // 오늘부터 7일 동안 빈 날짜 확인 후 Screening 복사 및 SeatStatus 생성
        for (int i = 0; i < 8; i++) {
            LocalDate targetDate = today.plusDays(i);
            long existingCount = screeningRepository.countByScreeningDate(targetDate);

            if (existingCount == 0) {
                // Screening 복사
                int copiedScreenings = screeningRepository.copyTodayScreeningsToDate(targetDate, earliestDate);
                System.out.println(targetDate + "에 " + copiedScreenings + "개 Screening 추가됨");

                // SeatStatus 생성 (해당 Screening의 Auditorium에 맞는 Seat 데이터 추가)
                List<Screening> newScreenings = screeningRepository.findAllByScreeningDate(targetDate);
                for (Screening screening : newScreenings) {
                    List<Seat> seats = seatRepository
                            .findByAuditorium_AuditoriumNo(screening.getAuditorium().getAuditoriumNo());

                    List<SeatStatus> seatStatuses = new ArrayList<>();
                    seats.forEach(seat -> seatStatuses.add(
                            SeatStatus.builder()
                                    .screening(screening)
                                    .seat(seat)
                                    .seatStatusEnum(SeatStatusEnum.AVAILABLE)
                                    .build()));

                    if (!seatStatuses.isEmpty()) {
                        seatStatusRepository.saveAll(seatStatuses);
                        System.out.println(
                                screening.getScreeningId() + "의 SeatStatus " + seatStatuses.size() + "개 추가됨");
                    } else {
                        System.out.println(
                                screening.getScreeningId() + "의 SeatStatus가 생성되지 않음 (해당 Auditorium에 Seat 없음)");
                    }
                }
            } else {
                System.out.println(targetDate + "에는 이미 데이터가 있습니다.");
            }
        }

    }

}
