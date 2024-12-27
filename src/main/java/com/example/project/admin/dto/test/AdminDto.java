package com.example.project.admin.dto.test;

import com.example.project.admin.Entity.constant.AdminRole;
import com.example.project.entity.constant.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminDto {
    private Long ano;
    private String userId;
    private String password;
    private MemberRole role;
}
