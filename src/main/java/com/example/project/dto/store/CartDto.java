package com.example.project.dto.store;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDto {
    private Long id;

    private Long memberId;
}
