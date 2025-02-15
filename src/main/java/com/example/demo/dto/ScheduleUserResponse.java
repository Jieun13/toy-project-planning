package com.example.demo.dto;

import com.example.demo.domain.InviteStatus;
import com.example.demo.domain.ScheduleRole;
import com.example.demo.domain.ScheduleUser;
import com.example.demo.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleUserResponse {
    private Long id;
    private String scheduleName;
    private Long scheduleId;
    private String email;
    private ScheduleRole role;
    private InviteStatus status;

    public static ScheduleUserResponse fromEntity(ScheduleUser scheduleUser) {
        return ScheduleUserResponse.builder()
                .id(scheduleUser.getId())
                .scheduleId(scheduleUser.getSchedule().getId())
                .scheduleName(scheduleUser.getSchedule().getTitle())
                .email(scheduleUser.getSchedule().getAuthor())
                .role(scheduleUser.getRole())
                .status(scheduleUser.getStatus())
                .build();
    }
}