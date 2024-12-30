package com.example.project.dto.reserve;

import java.time.LocalDateTime;
import java.util.List;

import com.example.project.entity.constant.ReserveStatus;

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
    private String screeningTime;
    private int totalPrice;

    private String theaterName;

    private Long auditoriumNo;
    private String auditoriumName;

    private List<Long> seatNumbers;

    private Long mid;
    private String memberId;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

}
