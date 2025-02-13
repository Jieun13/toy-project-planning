package com.example.demo.controller;

import com.example.demo.domain.Schedule;
import com.example.demo.dto.ScheduleRequest;
import com.example.demo.dto.ScheduleResponse;
import com.example.demo.service.ScheduleService;
import com.example.demo.user.config.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "새 일정 생성", description = "새로운 일정을 생성한다.")
    public ResponseEntity<ScheduleResponse> create(
            @RequestBody ScheduleRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Schedule newSchedule = scheduleService.save(request, email);
        return ResponseEntity.ok(ScheduleResponse.fromEntity(newSchedule));
    }

    @GetMapping
    @Operation(summary="사용자 일정 전체 조회", description="사용자가 작성한 전체 일정을 조회한다.")
    public ResponseEntity<List<ScheduleResponse>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date) {

        List<Schedule> schedules;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 날짜 필터가 있을 경우, 특정 기간 조회
        if (start_date != null && end_date != null) {
            schedules = scheduleService.findByDateRangeAndAuthor(start_date.atStartOfDay(), end_date.atTime(LocalTime.MAX), email);
        } else {
            // 필터가 없으면 전체 일정 조회
            schedules = scheduleService.findAllByEmail(email);
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

    @PutMapping("/{scheduleId}")
    @Operation(summary="일정 수정", description="일정을 수정한다.")
    public ResponseEntity<ScheduleResponse> update(@PathVariable("scheduleId") Long scheduleId, @RequestBody ScheduleRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Schedule schedule = scheduleService.update(scheduleId, request, email);
        return ResponseEntity.ok(ScheduleResponse.fromEntity(schedule));
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary="일정 삭제", description="일정을 삭제한다.")
    public ResponseEntity<ScheduleResponse> delete(@PathVariable("scheduleId") Long scheduleId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        scheduleService.delete(scheduleId, email);
        return ResponseEntity.noContent().build();
    }
}