package com.example.project.entity.reserve;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Theater {
    @SequenceGenerator(name = "theater_seq_gen", sequenceName = "theater_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "theater_seq_gen")
    @Id
    private Long theaterId; // 상영관번호

    @Column(nullable = false)
    private String theaterName; // 극장 이름

    @Column(nullable = false)
    private String theaterState; // 극장 지역

    @Column(length = 200)
    private String theaterAdd; // 극장 주소

}
