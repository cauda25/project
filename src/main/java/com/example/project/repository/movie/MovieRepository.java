package com.example.project.repository.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.project.entity.Genre;
import com.example.project.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieCustomRepository {
    // 해당 장르들을 하나라도 포함하는 영화 리스트
    @Query("SELECT m FROM Movie m JOIN m.movieGenres mg JOIN mg.genre g WHERE g IN :genres")
    List<Movie> findMoviesByGenres(List<Genre> genres);

    // 해당 감독의 영화 리스트
    @Query("SELECT m FROM Movie m JOIN m.moviePeople mp JOIN mp.person p WHERE p.id = :directorId AND p.job = 'Directing'")
    List<Movie> findMoviesByDirectorId(Long directorId);
}
