package com.example.project.service;

import java.util.List;

import com.example.project.entity.Movie;

public interface MemberFavoriteMovieService {
    // 찜한 목록에 추가
    void addFavoriteMovie(Long memberId, Long movieId);

    // 해당 멤버의 찜한 목록 리스트
    List<Movie> getFavoriteMoviesByMemberId(Long memberId);

    // MemberFavoriteMovie 테이블에서 중복 여부 체크
    boolean existsByMemberIdAndMovieId(Long memberId, Long movieId);

    // 찜한 영화 목록에서 해당 멤버의 해당 영화 제거
    void deleteFavoriteMovie(Long memberId, Long movieId);
}
