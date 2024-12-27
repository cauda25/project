package com.example.project.dto.reserve;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ScreeningDto {
    private Long screeningId;
    private String startTime;

    private String movieTitle;
    private String runtime;

    private LocalDate openDate;
    private LocalDate closeDate;

    private Long auditoriumNo;
    private String auditoriumName;

    private int availableSeats;
    private int totalSeats;
}
