package com.example.project.dto;

import java.time.LocalDateTime;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewDto {
    private Long rid; // 리뷰 ID
    @NotNull
    private String memberId;
    @NotNull
    private Long mid; // 회원 ID
    private Long id; // 영화 ID
    private String content; // 리뷰 내용
    private int rating; // 평점 (1~5)
}