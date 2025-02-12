package com.example.demo.controller;

import com.example.demo.domain.Schedule;
import com.example.demo.dto.ScheduleRequest;
import com.example.demo.dto.ScheduleResponse;
import com.example.demo.service.ScheduleService;
import com.example.demo.user.config.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final TokenProvider tokenProvider;

    @PostMapping
    @Operation(summary = "새 일정 생성", description = "새로운 일정을 생성한다.")
    public ResponseEntity<ScheduleResponse> create(
            @RequestBody ScheduleRequest request,
            @RequestHeader("Authorization") String authorizationHeader) { // 🔹 헤더에서 토큰 직접 받기

        // 🔹 Bearer 토큰에서 실제 토큰 값만 추출
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
        String token = authorizationHeader.substring(7); // "Bearer " 부분 제거

        // 🔹 토큰에서 사용자 정보 추출
        Authentication authentication = tokenProvider.getAuthentication(token);
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
        String username = authentication.getName();
        System.out.println(username); // 🔵 인증된 사용자 확인

        Schedule newSchedule = scheduleService.save(request, username);
        System.out.println(request.getStartDateTime() + " " + request.getEndDateTime());
        System.out.println(newSchedule.getStartTime() + " " + newSchedule.getEndTime());
        return ResponseEntity.ok(ScheduleResponse.fromEntity(newSchedule));
    }

    @GetMapping
    @Operation(summary="일정 전체 조회", description="전체 일정을 조회한다.")
    public ResponseEntity<List<ScheduleResponse>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date) {
        List<Schedule> schedules;

        // 날짜 필터가 있을 경우, 특정 기간 조회
        if (start_date != null && end_date != null) {
            schedules = scheduleService.findByDateRange(start_date.atStartOfDay(), end_date.atTime(LocalTime.MAX));
        } else {
            // 필터가 없으면 전체 일정 조회
            schedules = scheduleService.findAll();
        }

        List<ScheduleResponse> responses = schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary="특정 일정 조회", description="특정 일정을 조회한다.")
    public ResponseEntity<ScheduleResponse> getById(@PathVariable("scheduleId") Long scheduleId) {
        Schedule schedule = scheduleService.findById(scheduleId);
        return ResponseEntity.ok(ScheduleResponse.fromEntity(schedule));
    }

    @GetMapping("/api/{userId}")
    @Operation(summary="유저 아이디로 일정 조회", description="유저가 생성한 일정을 조회한다.")
    public ResponseEntity<List<ScheduleResponse>> getByUserId(@PathVariable("userId") Long userId) {
        List<Schedule> schedules = scheduleService.findByUserId(userId);
        List<ScheduleResponse> responses = schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{scheduleId}")
    @Operation(summary="일정 수정", description="일정을 수정한다.")
    public ResponseEntity<ScheduleResponse> update(@PathVariable("scheduleId") Long scheduleId, @RequestBody ScheduleRequest request, Principal principal) {
        Schedule schedule = scheduleService.update(scheduleId, request, principal.getName());
        return ResponseEntity.ok(ScheduleResponse.fromEntity(schedule));
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary="일정 삭제", description="일정을 삭제한다.")
    public ResponseEntity<ScheduleResponse> delete(@PathVariable("scheduleId") Long scheduleId) {
        scheduleService.delete(scheduleId);
        return ResponseEntity.noContent().build();
    }
}