package com.example.project.repository.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.MovieGenre;
import com.example.project.entity.MovieGenreId;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenreId> {

    List<MovieGenre> findByIdMovieId(Long movieId);

}
