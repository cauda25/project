package com.example.project.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.MoviePerson;

public interface MoviePersonRepository extends JpaRepository<MoviePerson, Long> {
    // MoviePerson 테이블에서 중복 여부 체크
    boolean existsByMovieIdAndPersonId(Long movieId, Long personId);
}
