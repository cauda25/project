package com.example.project.admin.dto.test;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminInquiryDto {
    private Long id;

    private String name;

    private String email;

    private String content;

    private String answer;

    public AdminInquiryDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
