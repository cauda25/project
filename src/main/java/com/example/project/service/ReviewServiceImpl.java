package com.example.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        public void saveReview(ReviewDto reviewDto) {
                // 리뷰 엔티티 생성
                Review review = Review.builder()
                                .content(reviewDto.getContent())
                                .rating(reviewDto.getRating())
                                // 다른 필요한 필드들을 설정
                                .build();

                // 영화와 회원 설정 (기존 로직)
                Movie movie = movieRepository.findById(reviewDto.getId())
                                .orElseThrow(() -> new IllegalArgumentException("영화 정보를 찾을 수 없습니다."));
                review.setMovie(movie);

                Member member = memberRepository.findById(reviewDto.getMid())
                                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
                review.setMember(member);

                // 생성 시간 설정
                review.setCreatedAt(LocalDateTime.now());

                // 리뷰 저장
                reviewRepository.save(review);
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

        @Override
        public List<ReviewDto> getAllReviews() {
                return reviewRepository.findAll()
                                .stream()
                                .map(this::entityToDto)
                                .collect(Collectors.toList());
        }

        private ReviewDto entityToDto(Review review) {
                return ReviewDto.builder()
                                .rid(review.getRid())
                                .memberId(review.getMember().getMemberId())
                                .mid(review.getMember().getMid())
                                .id(review.getMovie().getId())
                                .content(review.getContent())
                                .rating(review.getRating())
                                .build();
        }

        public List<ReviewDto> findReviewsByMovieId(Long movieId) {
                // 영화 ID에 해당하는 리뷰 엔티티 리스트 조회
                List<Review> reviews = reviewRepository.findByMovieId(movieId);

                // 엔티티를 DTO로 변환하여 반환
                return reviews.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
        }

        private ReviewDto convertToDto(Review review) {
                return ReviewDto.builder()
                                .rid(review.getRid())
                                // 아래 두 메서드는 Member 엔티티에서 회원 식별 정보를 가져온다고 가정합니다.
                                .memberId(review.getMember().getMemberId()) // 예: 회원 고유 문자열 ID
                                .mid(review.getMember().getMid()) // 예: 회원의 Long 타입 ID
                                .id(review.getMovie().getId()) // 영화 ID
                                .content(review.getContent())
                                .rating(review.getRating())
                                .build();
        }

}
