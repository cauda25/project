package com.example.project.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.dto.reserve.ReserveDto;
import com.example.project.dto.reserve.ScreeningDto;
import com.example.project.dto.reserve.SeatStatusDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.SeatStatusEnum;
import com.example.project.entity.reserve.Reserve;

import com.example.project.service.reservation.ReserveService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/reservation")
public class ReserveController {
    private final ReserveService reserveService;

    @GetMapping
    public String getReservationPage(Model model) {
        // 지역 목록 제공
        List<String> regions = reserveService.getAllRegions();
        if (regions == null || regions.isEmpty()) {
            System.out.println("지역값이 없습니다");
        } else {
            System.out.println("Regions: " + regions);
        }
        model.addAttribute("regions", regions);
        return "/movie/reservation";
    }

    @GetMapping("/theaters")
    public ResponseEntity<List<TheaterDto>> getTheatersByRegion(@RequestParam String region) {
        List<TheaterDto> theaters = reserveService.getTheatersByRegion(region);
        if (theaters == null || theaters.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/movies")
    public ResponseEntity<List<String>> getMoviesByTheater(@RequestParam Long theaterId) {
        List<String> movies = reserveService.getMoviesByTheaterId(theaterId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/screenings")
    public ResponseEntity<List<ScreeningDto>> getScreenings(
            @RequestParam String movieTitle,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        System.out.println("Received date: " + date);
        List<ScreeningDto> screeningDtos = reserveService.getScreeningsByMovieAndDate(movieTitle, date);
        screeningDtos.forEach(screeningDto -> {
            Map<String, Integer> seatCounts = reserveService.getSeatCounts(screeningDto.getScreeningId());
            screeningDto.setAvailableSeats(seatCounts.get("availableSeats"));
            screeningDto.setTotalSeats(seatCounts.get("totalSeats"));
        });
        return ResponseEntity.ok(screeningDtos);
    }

    @GetMapping("/seat_sell")
    public String getSeatSellPage(@RequestParam Long screeningId, Model model) {
        log.info("Seat sell page requested for screening ID: " + screeningId);

        // 상영 정보 확인 및 전달
        ScreeningDto screening = reserveService.getScreeningById(screeningId);
        if (screening == null) {
            log.error("Invalid screening ID: " + screeningId);
            return "error/404"; // 존재하지 않는 경우 404 페이지로 리다이렉트
        }

        model.addAttribute("screening", screening);
        model.addAttribute("screeningId", screeningId);

        return "movie/seat_sell";
    }

    @GetMapping("/seats/{screeningId}")
    @ResponseBody
    public ResponseEntity<List<SeatStatusDto>> getSeatsByScreeningId(@PathVariable Long screeningId) {
        try {
            List<SeatStatusDto> seatStatuses = reserveService.getSeatStatusesByScreening(screeningId);
            return ResponseEntity.ok(seatStatuses);
        } catch (Exception e) {
            log.error("Error fetching seats for screening ID: " + screeningId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping("/update-seat-status")
    public ResponseEntity<Map<String, Object>> updateSeatStatus(@RequestBody Map<String, Object> payload) {
        try {
            List<Map<String, Object>> seats = (List<Map<String, Object>>) payload.get("seats");
            String status = (String) payload.get("status");

            for (Map<String, Object> seat : seats) {
                Long seatStatusId = Long.valueOf(seat.get("seatStatusId").toString()); // seatStatusId 사용
                reserveService.updateSeatStatus(seatStatusId, SeatStatusEnum.valueOf(status));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            // 예약된 좌석 처리
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            // 기타 예외 처리
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<?> completePayment(@RequestBody ReserveDto reserveDto, HttpSession session) {
        try {
            // 세션에서 회원 정보 가져오기
            Member loggedInMember = (Member) session.getAttribute("loggedInMember");
            if (loggedInMember == null) {
                throw new IllegalStateException("로그인된 회원이 없습니다.");
            }

            // 회원 정보를 ReserveDto에 매핑
            reserveDto.setMid(loggedInMember.getMid()); // PK 값
            reserveDto.setMemberId(loggedInMember.getMemberId()); // 회원가입 시 설정된 ID

            // 예약 정보 저장
            Reserve reserve = reserveService.saveReservation(reserveDto);

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("reserveId", reserve.getReserveId());
            response.put("reserveNo", reserve.getReserveNo());
            response.put("totalPrice", reserve.getTotalPrice());
            for (Long seatStatusId : reserveDto.getSeatNumbers()) {
                reserveService.updateSeatStatus(seatStatusId, SeatStatusEnum.SOLD);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 처리
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}