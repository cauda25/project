package com.example.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.entity.constant.MemberRole;
import com.example.project.repository.movie.MovieRepository;
import com.example.project.service.MemberService;

import jakarta.transaction.Transactional;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MovieRepository movieRepository;

    // @Autowired
    // private ReservationRepository reservationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testInsert() {

        Member member = Member.builder()
                .memberId("user0309")
                .password(passwordEncoder.encode("1111"))
                .email("user33@example.com")
                .name("가렌")
                .point(0)
                .gender("Male")
                .birth("2000-03-09")
                .role(MemberRole.MEMBER)
                .phone("01012345678")
                // .address("address1")
                .build();

        // 저장
        memberRepository.save(member);
        // 저장 확인
    }

    @Test
    public void isLogin() {
        System.out.println(memberRepository.findByMemberId("user1"));
    }

    // @Test
    // @Transactional
    // @Rollback(value = false)
    // public void testInsertReservationWithBuilder() {
    // // 1. 기존 Member와 Movie 가져오기
    // Member member = memberRepository.findById(1L)
    // .orElseThrow(() -> new RuntimeException("Member not found"));
    // Movie movie = movieRepository.findById(845781L)
    // .orElseThrow(() -> new RuntimeException("Movie not found"));

    // // 2. Reservation 생성
    // Reservation reservation = Reservation.builder()
    // .member(member)
    // .movie(movie)
    // .reservationDate(LocalDateTime.now())
    // .seatNumber("A1")
    // .build();

    // // 3. 저장
    // reservationRepository.save(reservation);

    // // 4. 확인
    // }
}
