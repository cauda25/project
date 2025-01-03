package com.example.project.service.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.project.dto.MovieDto;
import com.example.project.dto.reserve.ReserveDto;
import com.example.project.dto.reserve.ScreeningDto;
import com.example.project.dto.reserve.SeatDto;
import com.example.project.dto.reserve.SeatStatusDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.entity.constant.ReserveStatus;
import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.Auditorium;
import com.example.project.entity.reserve.Reserve;
import com.example.project.entity.reserve.Screening;
import com.example.project.entity.reserve.Seat;
import com.example.project.entity.reserve.SeatStatus;
import com.example.project.entity.reserve.Theater;

public interface ReserveService {

        List<ReserveDto> getAllReserves();

        List<String> getAllRegions();

        List<TheaterDto> getTheatersByRegion(String region);

        List<String> getMoviesByTheaterId(Long theaterId);

        List<ScreeningDto> getScreeningsByMovieAndDate(String movieTitle, LocalDate date);

        List<SeatStatusDto> getSeatStatuses(Long screeningId);

        Map<String, Integer> getSeatCounts(Long screeningId);

        List<SeatStatusDto> getSeatStatusesByScreening(Long screeningId);

        ScreeningDto getScreeningById(Long screeningId);

        void updateSeatStatus(Long seatStatusId, SeatStatusEnum seatStatusEnum);

        void resetExpiredReservations();

        Long generateReservationNumber();

        int calculateTotalPrice(List<SeatStatus> seatStatuses);

        Reserve saveReservation(ReserveDto reserveDto);

        // void addSeatToReserve(Reserve reserve, SeatStatus seatStatus);

        void cancelReservation(Long reserveId);

        List<ReserveDto> getMemberReservations(Long mid);

        default Reserve dtoToEntity(ReserveDto reserveDto) {
                return Reserve.builder()
                                .reserveId(reserveDto.getReserveId())
                                .reserveNo(reserveDto.getReserveNo())
                                .reserveStatus(ReserveStatus.valueOf(reserveDto.getReserveStatus()))
                                .movieTitle(reserveDto.getMovieTitle())
                                .screeningTime(reserveDto.getScreeningTime())
                                .totalPrice(reserveDto.getTotalPrice())
                                .member(Member.builder().mid(reserveDto.getMid()).build())
                                .build();
        }

        default ReserveDto entityToDto(Reserve reserve) {
                List<SeatDto> seatDtos = reserve.getSeatStatuses().stream()
                                .map(seatStatus -> new SeatDto(
                                                seatStatus.getSeat().getSeatId(),
                                                seatStatus.getSeat().getRowNum(),
                                                seatStatus.getSeat().getSeatNum(),
                                                null, // auditoriumNo (옵션)
                                                null // auditoriumName (옵션)
                                ))
                                .collect(Collectors.toList());
                return ReserveDto.builder()
                                .reserveId(reserve.getReserveId())
                                .reserveNo(reserve.getReserveNo())
                                .reserveStatus(reserve.getReserveStatus().toString())
                                .movieTitle(reserve.getMovieTitle())
                                .screeningTime(reserve.getScreeningTime())
                                .totalPrice(reserve.getTotalPrice())
                                .mid(reserve.getMember().getMid())
                                .memberId((reserve.getMember().getMemberId()))
                                .seats(seatDtos)
                                .seatStatuses(
                                                reserve.getSeatStatuses().stream()
                                                                .map(SeatStatus::getSeatStatusId)
                                                                .collect(Collectors.toList()))
                                .screeningDate(reserve.getSeatStatuses().isEmpty() ? null
                                                : reserve.getSeatStatuses().get(0).getScreening().getScreeningDate())
                                .theaterName(reserve.getSeatStatuses().isEmpty() ? null
                                                : reserve.getSeatStatuses().get(0).getScreening().getAuditorium()
                                                                .getTheater().getTheaterName())
                                .auditoriumName(reserve.getSeatStatuses().isEmpty() ? null
                                                : reserve.getSeatStatuses().get(0).getScreening().getAuditorium()
                                                                .getAuditoriumName())
                                .auditoriumNo(reserve.getSeatStatuses().isEmpty() ? null
                                                : reserve.getSeatStatuses().get(0).getScreening().getAuditorium()
                                                                .getAuditoriumNo())
                                .regDate(reserve.getRegDate())
                                .updateDate(reserve.getUpdateDate())
                                .build();

        }
}
