package com.example.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.MemberRole;
import com.example.project.service.MemberService;

import jakarta.transaction.Transactional;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

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
}
