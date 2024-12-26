package com.example.project.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationDto {
    private Long id;
    private Long memberId;
    private Long movieId;
    private LocalDateTime reservationDate;
    private String seatNumber;
    private String movieTitle;
    private String moviePosterPath;
}
