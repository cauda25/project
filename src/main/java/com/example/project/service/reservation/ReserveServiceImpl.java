package com.example.project.service.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.MovieDto;
import com.example.project.dto.reserve.ReserveDto;
import com.example.project.dto.reserve.ScreeningDto;
import com.example.project.dto.reserve.SeatStatusDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.entity.Reservation;
import com.example.project.entity.constant.ReserveStatus;
import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.Reserve;
import com.example.project.entity.reserve.Screening;
import com.example.project.entity.reserve.SeatStatus;
import com.example.project.entity.reserve.Theater;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.reserve.ReserveRepository;
import com.example.project.repository.reserve.ScreeningRepository;
import com.example.project.repository.reserve.SeatRepository;
import com.example.project.repository.reserve.SeatStatusRepository;
import com.example.project.repository.reserve.TheaterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReserveServiceImpl implements ReserveService {
    private final ReserveRepository reserveRepository;
    private final TheaterRepository theaterRepository;
    private final ScreeningRepository screeningRepository;
    private final MemberRepository memberRepository;
    private final SeatStatusRepository seatStatusRepository;

    @Override
    public List<ReserveDto> getAllReserves() {

        List<Reserve> reserves = reserveRepository.findAll();

        return reserves.stream()
                .map(reserve -> entityToDto(reserve))
                .collect(Collectors.toList());
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
    public void updateSeatStatus(Long seatStatusId, SeatStatusEnum targetStatus) {
        SeatStatus seatStatus = seatStatusRepository.findById(seatStatusId)
                .orElseThrow(() -> new IllegalArgumentException("좌석 정보를 찾을 수 없습니다. ID: " + seatStatusId));

        SeatStatusEnum currentStatus = seatStatus.getSeatStatusEnum();

        // 상태 전이 검증 로직
        if ((currentStatus == SeatStatusEnum.AVAILABLE && targetStatus == SeatStatusEnum.RESERVED) ||
                (currentStatus == SeatStatusEnum.RESERVED && targetStatus == SeatStatusEnum.SOLD)) {
            seatStatus.setSeatStatusEnum(targetStatus);
            seatStatusRepository.save(seatStatus);
        } else {
            throw new IllegalStateException("상태 전이가 허용되지 않습니다. 현재 상태: "
                    + currentStatus + ", 대상 상태: " + targetStatus);
        }
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
    public Long generateReservationNumber() {
        // 현재 날짜를 LocalDateTime으로 가져오기
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // 같은 날짜에 생성된 예약 건수를 카운트
        Long count = reserveRepository.countByRegDateBetween(startOfDay, endOfDay);

        // 예약 번호 생성 (현재 날짜 + 카운트 + 1)
        return Long.valueOf(
                String.format("%s%04d", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), count + 1));
    }

    @Override
    public int calculateTotalPrice(List<SeatStatus> seatStatuses) {
        return (int) seatStatuses.stream()
                .mapToLong(seatStatus -> seatStatus.getSeat().getAuditorium().getPrice())
                .sum();
    }

    @Transactional
    @Override
    public Reserve saveReservation(ReserveDto reserveDto) {
        Member member = memberRepository.findById(reserveDto.getMid())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. ID: " + reserveDto.getMid()));

        List<Long> seatStatusIds = reserveDto.getSeatStatuses();
        if (seatStatusIds == null || seatStatusIds.isEmpty()) {
            throw new IllegalArgumentException("좌석 정보가 없습니다.");
        }
        List<SeatStatus> seatStatuses = seatStatusRepository.findAllById(seatStatusIds);

        if (seatStatuses.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 seatNumbers입니다: " + seatStatusIds);
        }

        Reserve reserve = new Reserve();
        reserve.setReserveStatus(ReserveStatus.COMPLETE);
        reserve.setReserveNo(generateReservationNumber());
        reserve.setSeatStatuses(seatStatuses);
        reserve.setTotalPrice(calculateTotalPrice(seatStatuses));
        reserve.setMovieTitle(reserveDto.getMovieTitle());
        reserve.setScreeningTime(reserveDto.getScreeningTime());
        reserve.setMember(member);

        for (SeatStatus seatStatus : seatStatuses) {
            seatStatus.setReserve(reserve); // SeatStatus에 Reserve 설정
        }

        return reserveRepository.save(reserve);

    }

    @Override
    public void addSeatToReserve(Reserve reserve, SeatStatus seatStatus) {
        if (seatStatus.getSeatStatusEnum() != SeatStatusEnum.AVAILABLE) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }

        reserve.addSeatStatus(seatStatus);
        seatStatus.setSeatStatusEnum(SeatStatusEnum.RESERVED);
        seatStatusRepository.save(seatStatus);
    }

    @Override
    @Transactional
    public void cancelReservation(Long reserveId) {
        Reserve reserve = reserveRepository.findById(reserveId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예매를 찾을 수 없습니다."));

        if (reserve.getReserveStatus() == ReserveStatus.CANCEL) {
            throw new IllegalStateException("이미 취소된 예매입니다.");
        }

        // 상태를 CANCEL로 변경
        reserve.setReserveStatus(ReserveStatus.CANCEL);
        // 모든 좌석 상태를 AVAILABLE로 변경
        reserve.getSeatStatuses().forEach(seatStatus -> {
            seatStatus.setSeatStatusEnum(SeatStatusEnum.AVAILABLE);
            seatStatusRepository.save(seatStatus);
        });

        reserveRepository.save(reserve);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReserveDto> getMemberReservations(Long mid) {
        // 사용자 ID로 예매 정보 조회
        List<Reserve> reserves = reserveRepository.findByMemberMidWithJoins(mid);
        if (reserves.isEmpty()) {
            System.out.println("No reservations found for member ID: " + mid);
        } else {
            reserves.forEach(reserve -> {
                System.out.println("Reserve ID: " + reserve.getReserveId());
                reserve.getSeatStatuses().forEach(seatStatus -> {
                    System.out.println("Seat: " + seatStatus.getSeat().getRowNum() + seatStatus.getSeat().getSeatNum());
                    System.out.println("Screening Date: " + seatStatus.getScreening().getScreeningDate());
                });
            });
        }

        // 엔티티 → DTO 변환
        return reserves.stream()
                .map(reserve -> entityToDto(reserve))
                .collect(Collectors.toList());
    }
}
