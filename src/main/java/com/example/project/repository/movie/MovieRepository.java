package com.example.project.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieCustomRepository {

}
