package com.example.demo.controller;

import com.example.demo.dto.InviteRequest;
import com.example.demo.service.ScheduleService;
import com.example.demo.service.ScheduleUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleUserController {

    private final ScheduleUserService scheduleUserService;

    @PostMapping("/{scheduleId}/invitations")
    @Operation(summary = "사용자 초대", description = "일정에 사용자를 초대합니다.")
    public ResponseEntity<String> invite(@PathVariable("scheduleId") Long scheduleId, @RequestBody InviteRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        scheduleUserService.inviteUser(scheduleId, request, authentication.getName());
        return ResponseEntity.ok("초대가 완료되었습니다.");
    }

    @PatchMapping("/{scheduleId}/invitations/{invitationId}")
    @Operation(summary = "초대 응답", description = "사용자가 초대에 응답합니다.")
    public ResponseEntity<String> respondToInvite(@PathVariable("scheduleId") Long scheduleId,
                                                  @PathVariable("invitationId") Long invitationId,
                                                  @RequestParam boolean accept) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        scheduleUserService.respondToInvite(scheduleId, authentication.getName(), accept);
        return ResponseEntity.ok(accept ? "초대를 수락했습니다." : "초대를 거절했습니다.");
    }
}