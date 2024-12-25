package com.example.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project.entity.Member;
import com.example.project.entity.MemberFavoriteMovie;
import com.example.project.entity.Movie;
import com.example.project.repository.MemberFavoriteMovieRepository;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.movie.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberFavoriteMovieServiceImpl implements MemberFavoriteMovieService {

    private final MemberFavoriteMovieRepository favoriteMoviesRepository;
    private final MemberRepository MemberRepository;
    private final MovieRepository movieRepository;

    public void addFavoriteMovie(Long mid, Long movieId) {
        Member member = MemberRepository.findById(mid).get();
        Movie movie = movieRepository.findById(movieId).get();

        MemberFavoriteMovie favorite = new MemberFavoriteMovie();
        favorite.setMember(member);
        favorite.setMovie(movie);

        favoriteMoviesRepository.save(favorite);
    }

    public List<Movie> getFavoriteMoviesByMemberId(Long mid) {
        return favoriteMoviesRepository.findByMemberId(mid).stream()
                .map(MemberFavoriteMovie::getMovie)
                .collect(Collectors.toList());
    }

}
