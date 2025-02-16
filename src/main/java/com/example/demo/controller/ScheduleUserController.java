package com.example.demo.controller;

import com.example.demo.dto.InviteRequest;
import com.example.demo.dto.ScheduleUserResponse;
import com.example.demo.service.ScheduleService;
import com.example.demo.service.ScheduleUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleUserController {

    private final ScheduleUserService scheduleUserService;

    private String getAuthEmail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/{scheduleId}/invitations")
    @Operation(summary = "사용자 초대", description = "일정에 사용자를 초대합니다.")
    public ResponseEntity<String> invite(@PathVariable("scheduleId") Long scheduleId, @RequestBody InviteRequest request) {
        if(getAuthEmail().equals(request.getEmail())){
            return ResponseEntity.ok("본인은 초대할 수 없습니다.");
        }
        scheduleUserService.inviteUser(scheduleId, request, getAuthEmail());
        return ResponseEntity.ok("초대가 완료되었습니다.");
    }

    @GetMapping("/{scheduleId}/invitations")
    @Operation(summary = "초대된 사용자 목록 조회", description = "해당 일정에 초대된 사용자 목록을 조회합니다")
    public ResponseEntity<List<ScheduleUserResponse>> getInvitations(@PathVariable Long scheduleId) {
        List<ScheduleUserResponse> invitees = scheduleUserService.getInvitees(scheduleId);
        return ResponseEntity.ok(invitees);
    }

    @GetMapping("/{scheduleId}/users")
    @Operation(summary = "참여 요청을 수락한 참여자 목록 조회", description = "실제 참여자 목록을 조회합니다.")
    public ResponseEntity<List<ScheduleUserResponse>> getParticipants(@PathVariable Long scheduleId) {
        List<ScheduleUserResponse> participants = scheduleUserService.getParticipants(scheduleId);
        return ResponseEntity.ok(participants);
    }

    @DeleteMapping("/invitations/{invitationId}")
    @Operation(summary = "초대 취소", description = "초대한 사용자가 초대를 취소합니다.")
    public ResponseEntity<String> cancelInvitation(@PathVariable("invitationId") Long invitationId) {
        scheduleUserService.remove(invitationId);
        return ResponseEntity.ok("초대를 취소했습니다.");
    }

    @PatchMapping("/invitations/{invitationId}")
    @Operation(summary = "초대 응답", description = "사용자가 초대에 응답합니다.")
    public ResponseEntity<String> respondToInvite(@PathVariable("invitationId") Long invitationId,
                                                  @RequestParam boolean accept) {
        scheduleUserService.respondToInvite(invitationId, accept);
        return ResponseEntity.ok(accept ? "초대를 수락했습니다." : "초대를 거절했습니다.");
    }

    @GetMapping("/invitations")
    @Operation(summary = "초대 요청 목록", description = "사용자가 초대 받은 요청 목록을 조회합니다.")
    public ResponseEntity<List<ScheduleUserResponse>> getInvitationList() {
        List<ScheduleUserResponse> list = scheduleUserService.getInvitationList(getAuthEmail());
        return ResponseEntity.ok(list);
    }
}