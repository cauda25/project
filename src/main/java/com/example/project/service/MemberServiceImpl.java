package com.example.project.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.MemberRole;
import com.example.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerMember(MemberDto memberDto) {
        Member member = Member.builder()
                .memberId(memberDto.getMemberId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .birth(memberDto.getBirth())
                .gender(memberDto.getGender())
                .phone(memberDto.getPhone())
                .city(memberDto.getCity())
                .district(memberDto.getDistrict())
                .role(MemberRole.MEMBER)
                .point(0)
                .build();
        memberRepository.save(member);
    }

    @Override
    public boolean validateMember(String memberId, String rawPassword) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return passwordEncoder.matches(rawPassword, member.getPassword());
        }
        return false;
    }

    public MemberDto getMemberById(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .map(member -> MemberDto.builder()
                        .mid(member.getMid())
                        .memberId(member.getMemberId())
                        .name(member.getName())
                        .email(member.getEmail())
                        .phone(member.getPhone())
                        .point(member.getPoint())
                        .city(member.getCity())
                        .district(member.getDistrict())
                        .build())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public boolean verifyPassword(String memberId, String rawPassword) {
        return memberRepository.findByMemberId(memberId)
                .map(member -> passwordEncoder.matches(rawPassword, member.getPassword()))
                .orElse(false);
    }

    @Override
    public void updateMember(MemberDto memberDto) {
        Member member = memberRepository.findByMemberId(memberDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        member.setCity(memberDto.getCity());
        member.setDistrict(memberDto.getDistrict());

        // 비밀번호가 입력되었는지 확인하고 암호화 후 저장
        if (memberDto.getPassword() != null && !memberDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
            member.setPassword(encodedPassword);
        }

        memberRepository.save(member); // 수정된 정보 저장
    }

    @Override
    public boolean isMemberIdDuplicate(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }
}
