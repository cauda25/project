package com.example.project.entity.reserve;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "auditorium")
@Entity
public class Seat {
    @SequenceGenerator(name = "seat_seq_gen", sequenceName = "seat_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_seq_gen")
    @Id
    private Long seatId;

    @Column(nullable = false)
    private String rowNum; // 행

    @Column(nullable = false)
    private Long seatNum; // 열

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditorium_No", nullable = false)
    private Auditorium auditorium; // 상영관
}