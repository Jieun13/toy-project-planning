package com.example.demo.dto;

import com.example.demo.domain.Schedule;
import com.example.demo.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequest {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDateTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDateTime;

    @Builder
    public Schedule toEntity(String author) {
        return Schedule.builder()
                .title(title)
                .description(description)
                .startTime(startDateTime)
                .endTime(endDateTime)
                .author(author)
                .build();
    }
}