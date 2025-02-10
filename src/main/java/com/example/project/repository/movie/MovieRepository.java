package com.example.project.repository.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.project.entity.Genre;
import com.example.project.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieCustomRepository {

}
