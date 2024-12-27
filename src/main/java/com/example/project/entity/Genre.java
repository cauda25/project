package com.example.project.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@ToString(exclude = "movieGenres")
@Setter
@Getter
@Table(name = "genres")
@Entity
public class Genre extends BaseEntity {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "genre" )
    @JsonManagedReference
    private Set<MovieGenre> movieGenres;

}
