package com.example.project.service.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.project.dto.reserve.ReserveDto;
import com.example.project.dto.reserve.ScreeningDto;
import com.example.project.dto.reserve.SeatDto;
import com.example.project.dto.reserve.SeatStatusDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.ReserveStatus;
import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.Reserve;
import com.example.project.entity.reserve.SeatStatus;

public interface ReserveService {

        List<String> getAllRegions();

        List<TheaterDto> getTheatersByRegion(String region);

        List<String> getMoviesByTheaterId(Long theaterId);

        List<ScreeningDto> getScreeningsByMovieAndDate(String movieTitle, LocalDate date);

        List<SeatStatusDto> getSeatStatuses(Long screeningId);

        Map<String, Integer> getSeatCounts(Long screeningId);

        List<SeatStatusDto> getSeatStatusesByScreening(Long screeningId);

        void updateSeatStatus(Long seatStatusId, SeatStatusEnum seatStatusEnum);

        void resetExpiredReservations();

        Long generateReservationNumber();

        int calculateTotalPrice(List<SeatStatus> seatStatuses);

        Reserve saveReservation(ReserveDto reserveDto);

        void cancelReservation(Long reserveId);

        List<ReserveDto> getMemberReservations(Long mid);

        default Reserve dtoToEntity(ReserveDto reserveDto) {
                return Reserve.builder()
                                .reserveId(reserveDto.getReserveId())
                                .reserveNo(reserveDto.getReserveNo())
                                .reserveStatus(ReserveStatus.valueOf(reserveDto.getReserveStatus()))
                                .movieTitle(reserveDto.getMovieTitle())
                                .screeningTime(reserveDto.getScreeningTime())
                                .screeningDate(reserveDto.getScreeningDate())
                                .totalPrice(reserveDto.getTotalPrice())
                                .theaterName(reserveDto.getTheaterName())
                                .auditoriumName(reserveDto.getAuditoriumName())
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
                                .seats(seatDtos)
                                .seatStatuses(
                                                reserve.getSeatStatuses().stream()
                                                                .map(SeatStatus::getSeatStatusId)
                                                                .collect(Collectors.toList()))
                                .screeningDate(reserve.getScreeningDate())
                                .theaterName(reserve.getTheaterName())
                                .auditoriumName(reserve.getAuditoriumName())
                                .regDate(reserve.getRegDate())
                                .updateDate(reserve.getUpdateDate())
                                .build();

        }
}
