package com.example.project.service;

import java.util.Optional;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.MemberRole;

public interface MemberService {

    void registerMember(MemberDto memberDto);

    boolean validateMember(String memberId, String password);

    MemberDto getMemberById(String memberId);

    boolean verifyPassword(String memberId, String rawPassword);

    void updateMember(MemberDto memberDto);

    boolean isMemberIdDuplicate(String memberId);

    boolean isEmailDuplicate(String email);

    void deleteMember(Long memberId);

    Optional<Member> findByMid(Long mid);

}