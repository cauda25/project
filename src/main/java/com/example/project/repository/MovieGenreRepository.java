package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.MovieGenre;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long> {

}