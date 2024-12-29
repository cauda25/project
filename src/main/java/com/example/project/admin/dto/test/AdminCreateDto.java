package com.example.project.admin.dto.test;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateDto {
    private String title;
    private String overview;
    private String releaseDate;
    private List<Long> genreIds;
    private List<String> actors;
    private String directorName;
}
