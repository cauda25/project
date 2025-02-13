package com.example.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.transaction.Transactional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovieId(Long movieId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.member.mid = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

}