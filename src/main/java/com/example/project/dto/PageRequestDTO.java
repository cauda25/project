package com.example.project.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@ToString
@Setter
@Getter
public class PageRequestDTO {
    private int size;
    private int page;

    // 검색
    private String type;
    private String keyword;

    // 정렬
    private String sort;

    // 장르
    private Long genre;

    // 분류
    private String movieList;

    public PageRequestDTO() {
        this.page = 1;
        this.size = 8;
        this.sort = "popularity";
        this.genre = null;
        this.movieList = "nowPlaying";
    }

    public void updateSize(int newSize) {
        this.size = newSize;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

}
