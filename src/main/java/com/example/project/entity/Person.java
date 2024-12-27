package com.example.project.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@ToString(exclude = { "moviePeople" })
@Setter
@Getter
@Table(name = "people")
@Entity
public class Person extends BaseEntity {

    @Id
    private Long id;

    private Long gender;
    private String job;
    private String name;
    private Double popularity;
    private String profilePath;

    @OneToMany(mappedBy = "person")
    private Set<MoviePerson> moviePeople;

}
