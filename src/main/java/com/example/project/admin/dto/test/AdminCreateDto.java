package com.example.project.admin.dto.test;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateDto {
    @NotBlank(message = "영화 제목을 입력바랍니다.")
    private String title;
    private String overview;
    @NotBlank(message = "영화 상영일을 기입바랍니다.")
    private String releaseDate;
    @Size(min = 1)
    @NotNull(message = "장르를 최소 1개 이상 선택해야 합니다.")
    private List<Long> genreIds;
    @Size(min = 1, message = "해당 영화의 배우를 최소 1명이상 등록해주세요.")
    private List<String> actors;
    @NotBlank(message = "해당 영화의 감독을 등록해주세요.")
    private String directorName;
}
