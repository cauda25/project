package com.example.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project.dto.ReservationDto;
import com.example.project.entity.Movie;
import com.example.project.entity.Reservation;
import com.example.project.repository.ReservationRepository;
import com.example.project.repository.movie.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final MovieRepository movieRepository;

    @Override
    public List<ReservationDto> getMemberReservations(Long memberId) {
        List<Reservation> reservations = reservationRepository.findByMember_Mid(memberId);
        return reservations.stream().map(reservation -> {
            Movie movie = reservation.getMovie();
            return ReservationDto.builder()
                    .id(reservation.getId())
                    .memberId(reservation.getMember().getMid())
                    .movieId(movie.getId())
                    .reservationDate(reservation.getReservationDate())
                    .seatNumber(reservation.getSeatNumber())
                    .movieTitle(movie.getTitle())
                    .moviePosterPath(movie.getPosterPath())
                    .build();
        }).collect(Collectors.toList());
    }
}
