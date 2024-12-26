package com.example.project.dto;

import java.time.LocalDateTime;

import com.example.project.entity.BaseEntity;
import com.example.project.entity.constant.MemberRole;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long mid;

    @NotBlank(message = "아이디를 입력해주세요.")
    private String memberId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "성별을 선택해주세요.")
    private String gender;

    @NotBlank(message = "생년월일을 입력해주세요.")
    private String birth;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;

    @NotBlank(message = "주소를 입력해주세요.")
    private String city;

    @NotBlank(message = "주소를 입력해주세요.")
    private String district;

    private int point;

    @Enumerated(EnumType.STRING)
    private MemberRole role; // Enum을 String으로 변환하여 전송

    private LocalDateTime regDate; // 등록일 (읽기 전용)

}