package com.example.project.dto.reserve;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReserveDto {
    private Long reserveId; // 예매pk
    private Long reserveNo; // 예매번호
    private String reserveStatus;
    private String movieTitle;

    private String screeningDate;

    private String screeningTime;
    private int totalPrice;

    private String theaterName;
    private String auditoriumName;

    private List<Long> seatStatuses;
    private List<SeatDto> seats;

    private Long mid;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

}
