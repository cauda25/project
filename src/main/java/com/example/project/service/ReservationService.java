package com.example.project.service;

import java.util.List;

import com.example.project.dto.ReservationDto;

public interface ReservationService {
    List<ReservationDto> getMemberReservations(Long memberId);
}
