// package com.example.project.service;

// import java.time.LocalDateTime;
// import java.util.Optional;

// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.example.project.dto.AuthMemberDto;
// import com.example.project.dto.MemberDto;
// import com.example.project.entity.Member;
// import com.example.project.entity.constant.MemberRole;
// import com.example.project.repository.MemberRepository;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.log4j.Log4j2;

// @Log4j2
// @Service
// @RequiredArgsConstructor
// public class MemberLoginServiceImpl implements UserDetailsService {

// private final MemberRepository memberRepository;

// @Override
// public UserDetails loadUserByUsername(String username) throws
// UsernameNotFoundException {
// // 시큐리티 로그인 post 처리
// log.info("service username : {}", username);

// // 로그인 요청
// Optional<Member> result = memberRepository.findByMemberId(username);

// if (!result.isPresent()) {
// throw new UsernameNotFoundException("아이디나 비밀번호 확인");
// }

// // 이메일이 존재한다면 entity => dto 변경
// Member member = result.get();

// // 최종 로그인 날짜 수정
// accessLogin(member);

// MemberDto memberDto = MemberDto.builder()
// .mid(member.getMid())
// .memberId(member.getMemberId())
// .password(member.getPassword())
// .name(member.getName())
// .email(member.getEmail())
// .gender(member.getGender())
// .birth(member.getBirth())
// .phone(member.getPhone())
// .city(member.getCity())
// .district(member.getDistrict())
// .point(member.getPoint())
// .role(member.getRole())
// .build();

// return new AuthMemberDto(memberDto);
// }

// private void accessLogin(Member member) {

// member.setLastLogin(LocalDateTime.now());
// memberRepository.save(member);
// }
// }
