package com.example.demo.controller;

import com.example.demo.domain.Schedule;
import com.example.demo.dto.ScheduleRequest;
import com.example.demo.dto.ScheduleResponse;
import com.example.demo.service.ScheduleService;
import com.example.demo.service.ScheduleUserService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleUserService scheduleUserService;

    private static String getAuthEmail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping
    @Operation(summary = "새 일정 생성", description = "새로운 일정을 생성한다.")
    public ResponseEntity<ScheduleResponse> create(
            @RequestBody ScheduleRequest request) {
        Schedule newSchedule = scheduleService.save(request, getAuthEmail());
        return ResponseEntity.ok(ScheduleResponse.fromEntity(newSchedule));
    }

    @GetMapping
    @Operation(summary = "사용자 일정 전체 조회", description = "사용자의 전체 일정을 조회한다.")
    public ResponseEntity<List<ScheduleResponse>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String authEmail = getAuthEmail();
        List<Schedule> schedules = (startDate != null && endDate != null)
                ? scheduleService.findByDateRangeAndAuthor(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), authEmail)
                : scheduleService.findAllByEmail(authEmail);

        List<ScheduleResponse> responses = schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());

        responses.addAll(scheduleUserService.getInvitedSchedules(authEmail));

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
        Schedule schedule = scheduleService.update(scheduleId, request, getAuthEmail());
        return ResponseEntity.ok(ScheduleResponse.fromEntity(schedule));
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary="일정 삭제", description="일정을 삭제한다.")
    public ResponseEntity<ScheduleResponse> delete(@PathVariable("scheduleId") Long scheduleId) {
        scheduleService.delete(scheduleId, getAuthEmail());
        return ResponseEntity.noContent().build();
    }
}