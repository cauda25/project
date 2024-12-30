package com.example.project.entity.reserve;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Screening {
    @SequenceGenerator(name = "screening_seq_gen", sequenceName = "screening_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "screening_seq_gen")
    @Id
    private Long screeningId;

    private String startTime;

    private String movieTitle;

    private String runtime;

    private LocalDate screeningDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditorium_no", nullable = false)
    private Auditorium auditorium;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatStatus> seatStatuses = new ArrayList<>(); // 좌석 상태 정보

}
