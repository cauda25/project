package com.example.project.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "movies")
@Entity
public class Movie extends BaseEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO) // 데이터 정보 넣을 때 주석 처리
    private Long id;

    private String backdropPath;
    private Long budget;
    private String homepage;
    // private String origin_country;
    private String originalLanguage;
    private String originalTitle;
    @Column(length = 1000)
    private String overview;
    private Double popularity;
    private String posterPath;
    private String releaseDate;
    private Long revenue;
    private Long runtime;
    private String status;
    private String tagline;
    private String title;
    private Double voteAverage;
    private Long voteCount;

    // @OneToMany(mappedBy = "movie")
    // private Set<MovieGenre> movieGenres;

    // @OneToMany(mappedBy = "movie")
    // private Set<MoviePerson> moviePeople;

    // @OneToMany(mappedBy = "movie")
    // private List<Review> reviews;

    // @OneToMany(mappedBy = "movie")
    // private List<MemberFavoriteMovie> memberFavoriteMovies = new ArrayList<>();

}
