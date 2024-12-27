package com.example.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.dto.MovieDto;
import com.example.project.entity.Favorite;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.repository.FavoriteRepository;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.movie.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;

    public void addFavorite(String memberId, Long movieId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("영화 정보를 찾을 수 없습니다."));

        if (favoriteRepository.existsByMemberAndMovie(member, movie)) {
            throw new RuntimeException("이미 찜한 영화입니다.");
        }

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setMovie(movie);
        favoriteRepository.save(favorite);
    }

    @Override
    public List<MovieDto> getFavorites(String memberId) {
        // 예시 로직
        List<Favorite> favorites = favoriteRepository.findByMember_MemberId(memberId);
        return favorites.stream()
                .map(favorite -> mapToMovieDto(favorite.getMovie()))
                .collect(Collectors.toList());
    }

    // Movie를 MovieDto로 변환하는 메서드 추가
    private MovieDto mapToMovieDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .backdropPath(movie.getBackdropPath())
                .budget(movie.getBudget())
                .homepage(movie.getHomepage())
                .originalLanguage(movie.getOriginalLanguage())
                .originalTitle(movie.getOriginalTitle())
                .overview(movie.getOverview())
                .popularity(movie.getPopularity())
                .posterPath(movie.getPosterPath())
                .releaseDate(movie.getReleaseDate())
                .revenue(movie.getRevenue())
                .runtime(movie.getRuntime())
                .status(movie.getStatus())
                .tagline(movie.getTagline())
                .title(movie.getTitle())
                .voteAverage(movie.getVoteAverage())
                .voteCount(movie.getVoteCount())
                // 필요한 경우 다른 필드 추가
                .build();
    }

}
