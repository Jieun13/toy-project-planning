package com.example.demo.dto;

import com.example.demo.domain.ScheduleRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteRequest {
    private String email;
    private ScheduleRole role;
}