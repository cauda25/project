package com.example.project.repository.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.MoviePerson;

public interface MoviePersonRepository extends JpaRepository<MoviePerson, String> {
    // MoviePerson 테이블에서 중복 여부 체크
    boolean existsByMovieIdAndPersonId(Long movieId, Long personId);

    List<MoviePerson> findByMovieId(Long movieId);
}
