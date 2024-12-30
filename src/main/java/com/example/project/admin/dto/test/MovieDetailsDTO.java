package com.example.project.admin.dto.test;

public class MovieDetailsDTO {
    private Long mid;
    private String title;
    private String releaseDate;
    private String genres;
    private String name;

    public MovieDetailsDTO(Long mid, String title, String releaseDate, String genres) {
        this.mid = mid;
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public MovieDetailsDTO(Long mid, String title, String name) {
        this.mid = mid;
        this.title = title;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
