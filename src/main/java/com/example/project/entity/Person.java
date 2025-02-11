package com.example.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "people")
@Entity
public class Person extends BaseEntity {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO) // 데이터 정보 넣을 때 주석 처리
    private Long id;

    private Long gender;
    private String job;
    private String name;
    private Double popularity;
    private String profilePath;
}
