package com.example.project.service.reservation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project.dto.MovieDto;
import com.example.project.dto.reserve.ReserveDto;
import com.example.project.dto.reserve.ScreeningDto;
import com.example.project.dto.reserve.SeatStatusDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Movie;
import com.example.project.entity.reserve.Reserve;
import com.example.project.entity.reserve.Screening;
import com.example.project.entity.reserve.SeatStatus;
import com.example.project.entity.reserve.Theater;
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
    public List<ScreeningDto> getScreeningsByMovieAndDate(String movieTitle, LocalDate date) {
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
                        .openDate(screening.getOpenDate())
                        .closeDate(screening.getCloseDate())
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

}
