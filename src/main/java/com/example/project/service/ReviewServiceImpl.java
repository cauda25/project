package com.example.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project.dto.ReviewDto;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.entity.Review;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.ReviewRepository;
import com.example.project.repository.movie.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
        private final ReviewRepository reviewRepository;
        private final MemberRepository memberRepository; // Member용 Repository 필요
        private final MovieRepository movieRepository; // Movie용 Repository 필요

        @Override
        public Long saveReview(ReviewDto reviewDto) {
                System.out.println("Received Review DTO: " + reviewDto);

                Member member = memberRepository.findByMemberId(reviewDto.getMemberId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "회원 정보를 찾을 수 없습니다: " + reviewDto.getMemberId()));
                System.out.println("Found Member: " + member);

                Movie movie = movieRepository.findById(reviewDto.getId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "영화 정보를 찾을 수 없습니다: " + reviewDto.getId()));
                System.out.println("Found Movie: " + movie);

                Review review = Review.builder()
                                .content(reviewDto.getContent())
                                .rating(reviewDto.getRating())
                                .member(member)
                                .movie(movie)
                                .build();

                return reviewRepository.save(review).getRid();
        }

        private Review dtoToEntity(ReviewDto reviewDto, Member member, Movie movie) {
                return Review.builder()
                                .content(reviewDto.getContent())
                                .rating(reviewDto.getRating())
                                .member(member)
                                .movie(movie)
                                .build();
        }

        @Override
        public List<ReviewDto> getReviewsByMovie(Long id) {
                List<Review> reviews = reviewRepository.findByMovieId(id);
                return reviews.stream()
                                .map(review -> ReviewDto.builder()
                                                .rid(review.getRid())
                                                .memberId(review.getMember().getMemberId())
                                                .mid(review.getMember().getMid())
                                                .id(review.getMovie().getId())
                                                .content(review.getContent())
                                                .rating(review.getRating())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Override
        public ReviewDto getReviewById(Long rid) {
                Review review = reviewRepository.findById(rid)
                                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

                return ReviewDto.builder()
                                .rid(review.getRid())
                                .memberId(review.getMember().getMemberId())
                                .mid(review.getMember().getMid())
                                .id(review.getMovie().getId())
                                .content(review.getContent())
                                .rating(review.getRating())
                                .build();
        }

        // @Override
        // public ReviewDto getReview(Long reviewNo) {
        // return entityToDto(reviewRepository.findById(reviewNo).get());
        // }

        // @Override
        // public ReviewDto getReview(Long rid) {
        // return Review.builder()
        // .rid(reviewNo.getRid())
        // .rating(reviewDto.getRating())
        // .content(reviewDto.getContent())
        // .member(Member.builder().mid(reviewDto.getMid()).build())
        // .movie(Movie.builder().id(reviewDto.getId()).build())
        // .build();
        // }

}
