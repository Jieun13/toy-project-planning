package com.example.demo.dto;

import com.example.demo.domain.Schedule;
import com.example.demo.user.dto.UserResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String author;

    public static ScheduleResponse fromEntity(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .author(schedule.getAuthor())
                .build();
    }
}