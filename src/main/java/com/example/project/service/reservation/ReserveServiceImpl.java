package com.example.project.service.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.project.dto.MovieDto;
import com.example.project.dto.reserve.ReserveDto;
import com.example.project.dto.reserve.ScreeningDto;
import com.example.project.dto.reserve.SeatStatusDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Movie;
import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.Reserve;
import com.example.project.entity.reserve.Screening;
import com.example.project.entity.reserve.SeatStatus;
import com.example.project.entity.reserve.Theater;
import com.example.project.repository.reserve.ReserveRepository;
import com.example.project.repository.reserve.ScreeningRepository;
import com.example.project.repository.reserve.SeatRepository;
import com.example.project.repository.reserve.SeatStatusRepository;
import com.example.project.repository.reserve.TheaterRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReserveServiceImpl implements ReserveService {
    private final ReserveRepository reserveRepository;
    private final TheaterRepository theaterRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final SeatStatusRepository seatStatusRepository;

    @Override
    public Long createReserve(ReserveDto reserveDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createReserve'");
    }

    @Override
    public ReserveDto getReserve(Long reserveId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReserve'");
    }

    @Override
    public List<ReserveDto> getAllReserves() {
        // Reserve 엔티티 리스트를 조회
        List<Reserve> reserves = reserveRepository.findAll();

        // Reserve 엔티티를 ReserveDto로 변환하여 반환
        return reserves.stream()
                .map(reserve -> entityToDto(reserve))
                .collect(Collectors.toList());
    }

    @Override
    public ReserveDto updateReserve(Long reserveId, ReserveDto reserveDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateReserve'");
    }

    @Override
    public void deleteReserve(Long reserveId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteReserve'");
    }

    @Override
    public List<String> getAllRegions() {
        return theaterRepository.findAllRegions();
    }

    @Override
    public List<TheaterDto> getTheatersByRegion(String region) {

        System.out.println("Received region: " + region);
        List<Theater> theaters = theaterRepository.findByTheaterState(region);
        if (theaters.isEmpty()) {
            throw new IllegalArgumentException("No theaters found for the region: " + region);
        }
        System.out.println("Found theaters: " + theaters);
        return theaters.stream()
                .map(theater -> new TheaterDto(
                        theater.getTheaterId(),
                        theater.getTheaterName(),
                        theater.getTheaterState(),
                        theater.getTheaterAdd()))
                .collect(Collectors.toList());

    }

    @Override
    public List<String> getMoviesByTheaterId(Long theaterId) {
        List<Screening> screenings = screeningRepository.findScreeningsByTheaterId(theaterId);
        return screenings.stream()
                .map(Screening::getMovieTitle)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> getScreeningsByMovieAndDate(String movieTitle,
            LocalDate date) {
        movieTitle = movieTitle.trim();
        System.out.println("Service - movieTitle: " + movieTitle);
        System.out.println("Service - date: " + date);
        List<Screening> screenings = screeningRepository.findScreeningsByMovieAndDate(movieTitle, date);
        return screenings.stream()
                .map(screening -> ScreeningDto.builder()
                        .screeningId(screening.getScreeningId())
                        .startTime(screening.getStartTime())
                        .movieTitle(screening.getMovieTitle())
                        .runtime(screening.getRuntime())
                        .screeningDate(screening.getScreeningDate()) // screeningDate 필드 추가
                        .auditoriumNo(screening.getAuditorium().getAuditoriumNo())
                        .auditoriumName(screening.getAuditorium().getAuditoriumName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<SeatStatusDto> getSeatStatuses(Long screeningId) {
        List<SeatStatus> seatStatuses = seatStatusRepository.findByScreeningId(screeningId);
        return seatStatuses.stream()
                .map(seatStatus -> SeatStatusDto.builder()
                        .seatId(seatStatus.getSeat().getSeatId())
                        .rowNum(seatStatus.getSeat().getRowNum())
                        .seatNum(seatStatus.getSeat().getSeatNum())
                        .status(seatStatus.getSeatStatusEnum())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getSeatCounts(Long screeningId) {
        int availableSeats = seatStatusRepository.countAvailableSeats(screeningId);
        int totalSeats = seatStatusRepository.countTotalSeats(screeningId);

        Map<String, Integer> seatCounts = new HashMap<>();
        seatCounts.put("availableSeats", availableSeats);
        seatCounts.put("totalSeats", totalSeats);

        return seatCounts;
    }

    @Override
    public List<SeatStatusDto> getSeatStatusesByScreening(Long screeningId) {
        List<SeatStatus> seatStatuses = seatStatusRepository.findSeatStatusesByScreeningId(screeningId);

        return seatStatuses.stream()
                .map(seatStatus -> SeatStatusDto.builder()
                        .seatStatusId(seatStatus.getSeatStatusId())
                        .seatId(seatStatus.getSeat().getSeatId())
                        .rowNum(seatStatus.getSeat().getRowNum())
                        .seatNum(seatStatus.getSeat().getSeatNum())
                        .status(seatStatus.getSeatStatusEnum())
                        .price(seatStatus.getSeat().getAuditorium().getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ScreeningDto getScreeningById(Long screeningId) {

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid screening ID: " + screeningId));

        return ScreeningDto.builder()
                .screeningId(screening.getScreeningId())
                .startTime(screening.getStartTime())
                .movieTitle(screening.getMovieTitle())
                .runtime(screening.getRuntime())
                .screeningDate(screening.getScreeningDate())
                .auditoriumNo(screening.getAuditorium().getAuditoriumNo())
                .auditoriumName(screening.getAuditorium().getAuditoriumName())
                .build();
    }

    @Transactional
    @Override
    public void updateSeatStatus(Long seatStatusId, SeatStatusEnum seatStatusEnum) {
        SeatStatus seatStatus = seatStatusRepository.findById(seatStatusId)
                .orElseThrow(() -> new IllegalArgumentException("좌석 정보를 찾을 수 없습니다. ID: " + seatStatusId));
        if (seatStatus.getSeatStatusEnum() != SeatStatusEnum.AVAILABLE) {
            throw new IllegalStateException("이미 예약된 좌석입니다: " + seatStatusId);
        }
        seatStatus.setSeatStatusEnum(seatStatusEnum);
        seatStatusRepository.save(seatStatus);
    }

    @Override
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void resetExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<SeatStatus> expiredSeats = seatStatusRepository.findBySeatStatusEnumAndUpdateDateBefore(
                SeatStatusEnum.RESERVED, now.minusMinutes(5));

        expiredSeats.forEach(seat -> {
            seat.setSeatStatusEnum(SeatStatusEnum.AVAILABLE);
        });

        seatStatusRepository.saveAll(expiredSeats);
    }

    @Override
    public String generateReservationNumber() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = reserveRepository.countByRegDate(LocalDate.now()) + 1; // 당일 예약 순번
        return currentDate + String.format("%04d", count); // yyyyMMdd + 순번
    }

    @Override
    public Reserve saveReservation(Reserve reserve, List<Long> seatStatusIds) {
        // 예매번호 생성
        String reservationNumberString = generateReservationNumber();
        Long reservationNumber = Long.valueOf(reservationNumberString); // Long 타입으로 변환
        reserve.setReserveNo(reservationNumber);

        // 좌석 상태 업데이트 및 연결
        List<SeatStatus> seatStatuses = seatStatusRepository.findAllById(seatStatusIds);
        seatStatuses.forEach(seatStatus -> {
            if (seatStatus.getSeatStatusEnum() != SeatStatusEnum.AVAILABLE) {
                throw new IllegalStateException("예약할 수 없는 좌석이 포함되어 있습니다: " + seatStatus.getSeatStatusId());
            }
            seatStatus.setSeatStatusEnum(SeatStatusEnum.SOLD);
            reserve.addSeatStatus(seatStatus); // 양방향 관계 설정
        });

        // 예매 정보 저장
        return reserveRepository.save(reserve);
    }

    @Override
    public void addSeatToReserve(Reserve reserve, SeatStatus seatStatus) {
        if (seatStatus.getSeatStatusEnum() != SeatStatusEnum.AVAILABLE) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }

        reserve.addSeatStatus(seatStatus); // Reserve에 좌석 추가
        seatStatus.setSeatStatusEnum(SeatStatusEnum.RESERVED);
        seatStatusRepository.save(seatStatus); // 변경 사항 저장
    }

    @Override
    public void removeSeatFromReserve(Reserve reserve, SeatStatus seatStatus) {
        if (!reserve.getSeatStatuses().contains(seatStatus)) {
            throw new IllegalStateException("해당 좌석은 예매 목록에 없습니다.");
        }

        reserve.removeSeatStatus(seatStatus); // Reserve에서 좌석 제거
        seatStatus.setSeatStatusEnum(SeatStatusEnum.AVAILABLE);
        seatStatusRepository.save(seatStatus); // 변경 사항 저장
    }

}
