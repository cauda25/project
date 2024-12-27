package com.example.project.service;

import java.util.List;

import com.example.project.dto.GenreDto;
import com.example.project.entity.Genre;

public interface GenreService {
    // 장르 전체 리스트
    List<GenreDto> getGenres();

    public default GenreDto entityToDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
