package com.example.project.repository.movie;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.project.entity.Genre;
import com.example.project.entity.Movie;

public interface MovieCustomRepository {
    // type, keyword, movieList, genreId에 따른 페이징된 영화 리스트
    Page<Object[]> getTotalList(String type, String keyword, String movieList, Long genreId,
            Pageable pageable);

    // 해당 인물의 영화 리스트
    List<Movie> getMovieListByPersonId(Long id);

    // 해당 영화의 상세 정보
    Object[] getMovieDetailById(Long id);

    List<Movie> findMoviesByDirectorId(Long directorId);

    List<Movie> findMoviesByGenres(List<Genre> genres);

    // List<String> getDirectorList(Long id);

    // List<String> getActorList(Long id);

    // List<String> getGenreList(Long id);
}
