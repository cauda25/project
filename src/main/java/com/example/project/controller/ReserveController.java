package com.example.project.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.dto.AuthMemberDto;
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

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveReservationInfo(@RequestBody Map<String, String> request, HttpSession session) {
        session.setAttribute("theater", request.get("theater"));
        session.setAttribute("movie", request.get("movie"));
        session.setAttribute("date", request.get("date"));
        session.setAttribute("auditorium", request.get("auditorium"));
        session.setAttribute("time", request.get("time"));
        session.setAttribute("screeningId", request.get("screeningId"));

        return ResponseEntity.ok("예약 정보 저장 완료");
    }

    @PostMapping("/info")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getReservationInfo(HttpSession session) {
        Map<String, String> response = new HashMap<>();

        String theater = (String) session.getAttribute("theater");
        String movie = (String) session.getAttribute("movie");
        String date = (String) session.getAttribute("date");
        String auditorium = (String) session.getAttribute("auditorium");
        String time = (String) session.getAttribute("time");
        String screeningId = (String) session.getAttribute("screeningId");

        if (theater == null || movie == null || date == null || auditorium == null || time == null
                || screeningId == null) {
            response.put("error", "전달된 정보가 부족합니다.");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("theater", theater);
        response.put("movie", movie);
        response.put("date", date);
        response.put("auditorium", auditorium);
        response.put("time", time);
        response.put("screeningId", screeningId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/seat_sell")
    public String getSeatSellPage(HttpSession session, Model model) {
        String screeningId = (String) session.getAttribute("screeningId");
        if (screeningId == null) {
            log.error("screeningId가 세션에 없습니다.");
            return "error/404";
        }
        String theater = (String) session.getAttribute("theater");
        String movie = (String) session.getAttribute("movie");
        String date = (String) session.getAttribute("date");
        String auditorium = (String) session.getAttribute("auditorium");
        String time = (String) session.getAttribute("time");

        if (theater == null || movie == null || date == null || auditorium == null || time == null) {
            log.error("예약 정보가 세션에 없습니다.");
            return "error/404";
        }
        model.addAttribute("theater", theater);
        model.addAttribute("movie", movie);
        model.addAttribute("date", date);
        model.addAttribute("auditorium", auditorium);
        model.addAttribute("time", time);
        model.addAttribute("screeningId", screeningId);

        return "movie/seat_sell";
    }

    @GetMapping("/seats")
    @ResponseBody
    public ResponseEntity<List<SeatStatusDto>> getSeatsByScreeningId(HttpSession session) {
        String screeningIdStr = (String) session.getAttribute("screeningId");
        if (screeningIdStr == null) {
            log.error("Screening ID가 세션에 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
        try {
            Long screeningId = Long.parseLong(screeningIdStr);
            List<SeatStatusDto> seatStatuses = reserveService.getSeatStatusesByScreening(screeningId);
            return ResponseEntity.ok(seatStatuses);
        } catch (Exception e) {
            log.error("Error fetching seats for screening ID: " + screeningIdStr, e);
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
    public ResponseEntity<Map<String, Object>> completePayment(@RequestBody ReserveDto reserveDto,
            @AuthenticationPrincipal AuthMemberDto authMemberDto) {
        log.info("결제 요청: {}", reserveDto);
        Long mid = authMemberDto.getMemberId();
        // Long dummyMemberId = 1L;
        Map<String, Object> response = new HashMap<>();
        try {
            if (reserveDto.getSeatStatuses() == null || reserveDto.getSeatStatuses().isEmpty()) {
                log.error("[오류] 예약 좌석 정보가 없습니다.");
                response.put("success", false);
                response.put("error", "예약 좌석 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            reserveDto.setMid(mid);
            // reserveDto.setMid(dummyMemberId);

            // 예약 정보 저장
            Reserve reserve = reserveService.saveReservation(reserveDto);
            log.info(" [예매 저장 성공] 예약 ID: {}, 예약 번호: {}", reserve.getReserveId(), reserve.getReserveNo());
            for (Long seatStatusId : reserveDto.getSeatStatuses()) {
                reserveService.updateSeatStatus(seatStatusId, SeatStatusEnum.SOLD);
            }
            // 응답 데이터 구성
            response.put("success", true);
            response.put("reserveId", reserve.getReserveId());
            response.put("reserveNo", reserve.getReserveNo());
            response.put("totalPrice", reserve.getTotalPrice());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 에러 처리
            log.error(" [결제 처리 중 오류 발생]", e);
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestBody Map<String, Long> payload) {
        try {
            Long reserveId = Long.valueOf(payload.get("reserveId").toString());
            reserveService.cancelReservation(reserveId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "예매가 성공적으로 취소되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}