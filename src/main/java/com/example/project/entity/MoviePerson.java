package com.example.project.entity;

import java.util.UUID;

import com.example.project.admin.Entity.constant.StatusRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@ToString(exclude = { "movie", "person" })
@Setter
@Getter
@Table(name = "movie_person")
@Entity
public class MoviePerson extends BaseEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(length = 500)
    private String character;

    private String role;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString(); // 엔티티 저장 전 UUID 설정
    }

}
