package com.example.project.dto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewDto {
    private Long id; // 리뷰 ID (Optional)
    @NotNull
    private Long memberId;

    @NotNull
    private Long movieId; // 회원 ID
    private String content; // 리뷰 내용
    private int rating; // 평점 (1~5)
    private String createdAt; // 작성 시간
}