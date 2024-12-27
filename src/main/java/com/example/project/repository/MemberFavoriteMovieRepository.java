package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.entity.MemberFavoriteMovie;

import jakarta.transaction.Transactional;

public interface MemberFavoriteMovieRepository extends JpaRepository<MemberFavoriteMovie, Long> {
    // 해당 멤버의 찜한 영화 리스트
    @Query("SELECT mfm FROM MemberFavoriteMovie mfm WHERE mfm.member.id = :memberId")
    List<MemberFavoriteMovie> findByMemberId(Long memberId);

    // MemberFavoriteMovie 테이블에서 중복 여부 체크
    boolean existsByMemberMidAndMovieId(Long memberId, Long movieId);

    // 찜한 영화 목록에서 해당 멤버의 해당 영화 제거
    @Transactional
    @Modifying
    @Query("DELETE FROM MemberFavoriteMovie mfm WHERE mfm.member.id = :memberId AND mfm.movie.id = :movieId")
    void deleteByMemberIdAndMovieId(Long memberId, Long movieId);

    // List<MemberFavoriteMovie> findByMovieId(Long movieId);
}