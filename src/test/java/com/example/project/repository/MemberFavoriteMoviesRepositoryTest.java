package com.example.project.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.project.entity.MemberFavoriteMovie;
import com.example.project.repository.movie.MovieRepository;

@SpringBootTest
public class MemberFavoriteMoviesRepositoryTest {

    @Autowired
    private MemberFavoriteMovieRepository memberFavoriteMoviesRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertTest() {
        MemberFavoriteMovie memberFavoriteMovies = MemberFavoriteMovie.builder()
                .movie(movieRepository.findById(912649L).get())
                .member(memberRepository.findById(2L).get())
                .build();

        memberFavoriteMoviesRepository.save(memberFavoriteMovies);
    }

}
