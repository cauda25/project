package com.example.project.service.reservation;

import com.example.project.dto.reserve.SeatStatusDto;
import com.example.project.entity.reserve.Seat;
import com.example.project.entity.reserve.SeatStatus;

public interface SeatStatusService {
    default SeatStatus dtoToEntity(SeatStatusDto seatStatusDto) {
        return SeatStatus.builder()
                .seatStatusId(seatStatusDto.getSeatStatusId())
                .seatStatusEnum(seatStatusDto.getStatus())
                .seat((Seat.builder().seatId(seatStatusDto.getSeatId()).rowNum(seatStatusDto.getRowNum())
                        .seatNum(seatStatusDto.getSeatNum()).build()))
                .build();
    }

    default SeatStatusDto entityToDto(SeatStatus seatStatus) {
        return SeatStatusDto.builder()
                .seatStatusId(seatStatus.getSeatStatusId())
                .seatId(seatStatus.getSeat().getSeatId())
                .rowNum(seatStatus.getSeat().getRowNum())
                .seatNum(seatStatus.getSeat().getSeatNum())
                .status(seatStatus.getSeatStatusEnum())
                .auditoriumName(seatStatus.getSeat().getAuditorium().getAuditoriumName())
                .build();
    }
}
