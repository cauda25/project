package com.example.project.service;

import java.util.List;
import java.util.Optional;

import com.example.project.dto.ReviewDto;
import com.example.project.entity.Movie;
import com.example.project.entity.Member;
import com.example.project.entity.Review;

public interface ReviewService {
    void saveReview(ReviewDto reviewDto);

    // ReviewDto getReview(Long reviewNo);

    List<ReviewDto> getReviewsByMovie(Long id);

    ReviewDto getReviewById(Long rid);

    List<ReviewDto> getAllReviews();

    List<ReviewDto> findReviewsByMovieId(Long movieId);

}
