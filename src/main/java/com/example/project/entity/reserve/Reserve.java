package com.example.project.entity.reserve;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.project.entity.BaseEntity;
import com.example.project.entity.Member;
import com.example.project.entity.constant.ReserveStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "member")
@Entity
public class Reserve extends BaseEntity {
    @SequenceGenerator(name = "reserve_seq_gen", sequenceName = "reserve_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserve_seq_gen")
    @Id
    private Long reserveId; // 예매pk

    @Column(nullable = false, unique = true)
    private Long reserveNo; // 예매번호

    @Enumerated(EnumType.STRING)
    private ReserveStatus reserveStatus; // 예매상태

    private String movieTitle;

    private String screeningTime;

    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid", nullable = true)
    private Member member; // 회원 정보

    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatStatus> seatStatuses = new ArrayList<>(); // 좌석 목록

    public void addSeatStatus(SeatStatus seatStatus) {
        seatStatuses.add(seatStatus);
        seatStatus.setReserve(this); // 양방향 관계 설정
    }

    public void removeSeatStatus(SeatStatus seatStatus) {
        seatStatuses.remove(seatStatus);
        seatStatus.setReserve(null); // 양방향 관계 해제
    }
}
