package com.example.project.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieDto {
    private Long id;

    private String backdropPath;
    private Long budget;
    private String homepage;
    private String originalLanguage;
    private String originalTitle;

    private String overview;
    private Double popularity;
    private String posterPath;
    private String releaseDate;
    private Long revenue;
    private Long runtime;
    private String status;
    private String tagline;
    private String title;
    private Double voteAverage;
    private Long voteCount;

    private List<GenreDto> genreDtos;

    private List<PersonDto> personDtos;

    private List<MoviePersonDto> moviePersonDtos;
}
