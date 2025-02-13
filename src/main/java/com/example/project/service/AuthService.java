package com.example.project.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.entity.Member;
import com.example.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public Optional<Member> authenticate(String memberId, String password) {
        Optional<Member> optionalMember = memberRepository.findByMemberIdAndPassword(memberId, password);
        if (optionalMember.isPresent() && optionalMember.get().getStatusRole() != StatusRole.INACTIVE) {
            return optionalMember;
        }
        return Optional.empty();
    }

}
