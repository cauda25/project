package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InquiryDto {

    private String name;
    private String email;
    private String phone;
    private String mobile;
    private String InquiryType;
    private String title;
    private String content;
    private String username;
    private String inquiry;

}
