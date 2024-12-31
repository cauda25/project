package com.example.project.dto.reserve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SeatDto {
    private Long seatId;
    private String rowNum;
    private Long seatNum;
    private Long auditoriumNo;
    private String auditoriumName;

    @Override
    public String toString() {
        return rowNum + seatNum;
    }
}
