package com.example.project.service.reservation;

import java.util.List;

import com.example.project.dto.reserve.SeatDto;
import com.example.project.entity.reserve.Auditorium;
import com.example.project.entity.reserve.Seat;

public interface SeatService {

    List<SeatDto> getSeatsByAuditorium(Long auditoriumId);

    default Seat dtoToEntity(SeatDto seatDto) {
        return Seat.builder()
                .seatId(seatDto.getSeatId())
                .rowNum(seatDto.getRowNum())
                .seatNum(seatDto.getSeatNum())
                .auditorium(Auditorium.builder()
                        .auditoriumNo(seatDto.getAuditoriumNo())
                        .build())
                .build();
    }

    default SeatDto entityToDto(Seat seat) {
        return SeatDto.builder()
                .seatId(seat.getSeatId())
                .rowNum(seat.getRowNum())
                .seatNum(seat.getSeatNum())
                .auditoriumNo(seat.getAuditorium().getAuditoriumNo())
                .auditoriumName(seat.getAuditorium().getAuditoriumName())
                .build();
    }
}