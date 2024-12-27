package com.example.project.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Entity
public class People {

    @Id
    private Long id;

    private String birthday;
    private Long gender;
    private String knownForDepartment;
    private String name;
    private String placeOfBirth;
    private Double popularity;
    private String profilePath;

    @OneToMany(mappedBy = "people")
    private Set<MoviePeople> moviePeople;

}
