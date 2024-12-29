package com.example.project.admin.dto.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInquiryDto {
    private Long id;

    private String name;

    private String email;

    private String content;
}
