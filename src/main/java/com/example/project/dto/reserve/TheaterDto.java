package com.example.project.dto.reserve;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TheaterDto {

    private Long theaterId;

    // @NotBlank(message = "영화관 이름은 작성해주세요.")
    private String theaterName;

    // @NotBlank(message = "영화관 주소를 등록해주세요.")
    private String theaterAdd;

    // @NotBlank(message = "영화관의 지역을 다시한번 입력해주세요")
    private String theaterState;

    // @NotBlank(message = "영화관의 지역을 다시한번 입력해주세요")
    private Long sno;
    private String state;

    public TheaterDto(String theaterName, String theaterAdd, String state) {
        this.theaterName = theaterName;
        this.theaterAdd = theaterAdd;
        this.state = state;
    }

    public TheaterDto(Long theaterId, String theaterName, String theaterAdd, String state) {
        this.theaterName = theaterName;
        this.theaterAdd = theaterAdd;
        this.state = state;
        this.theaterId = theaterId;

    }
}
