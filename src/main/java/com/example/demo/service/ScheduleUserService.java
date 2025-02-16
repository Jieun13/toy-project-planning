package com.example.demo.service;

import com.example.demo.domain.InviteStatus;
import com.example.demo.domain.Schedule;
import com.example.demo.domain.ScheduleRole;
import com.example.demo.domain.ScheduleUser;
import com.example.demo.dto.InviteRequest;
import com.example.demo.dto.ScheduleResponse;
import com.example.demo.dto.ScheduleUserResponse;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.repository.ScheduleUserRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleUserService {
    private final ScheduleUserRepository scheduleUserRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public ScheduleUser inviteUser(Long scheduleId, InviteRequest request, String email) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
        User invitee = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("초대할 사용자를 찾을 수 없습니다."));

        if (!schedule.getAuthor().equals(email)) {
            throw new RuntimeException("일정 소유자만 초대할 수 있습니다.");
        }
        if (request.getEmail().equals(email)) {
            throw new RuntimeException("본인은 초대할 수 없습니다.");
        }
        if (isInvitedUser(scheduleId, request.getEmail())) {
            throw new RuntimeException("이미 초대된 사용자입니다.");
        }

        ScheduleUser scheduleUser = ScheduleUser.builder()
                .schedule(schedule)
                .user(invitee)
                .role(ScheduleRole.VIEWER)
                .status(InviteStatus.PENDING)
                .build();

        return scheduleUserRepository.save(scheduleUser);
    }

    private boolean isInvitedUser(Long scheduleId, String email) {
        List<ScheduleUser> tableUsers = scheduleUserRepository.findByUserEmail(email);

        return tableUsers.stream()
                .anyMatch(scheduleUser -> scheduleUser.getSchedule().getId().equals(scheduleId)
                        && !scheduleUser.getStatus().equals(InviteStatus.DECLINED));
    }

    @Transactional
    public void respondToInvite(Long scheduleUserId, boolean accept) {
        ScheduleUser scheduleUser = scheduleUserRepository
                .findById(scheduleUserId)
                .orElseThrow(() -> new RuntimeException("ScheduleUser not found"));
        if (accept){
            scheduleUser.setStatus(InviteStatus.ACCEPTED);
        }
        else {
            scheduleUser.setStatus(InviteStatus.DECLINED);
        }
    }

    public List<ScheduleUserResponse> getInvitees (Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));

        List<ScheduleUser> users = scheduleUserRepository.findScheduleUsersByScheduleId(scheduleId);

        return users.stream().map(user -> ScheduleUserResponse.builder()
                        .id(user.getId())
                        .scheduleId(user.getSchedule().getId())
                        .scheduleName(user.getSchedule().getTitle())
                        .email(user.getUser().getEmail())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ScheduleUserResponse> getParticipants(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));

        List<ScheduleUser> users = scheduleUserRepository.findScheduleUsersByScheduleId(scheduleId);
        List<ScheduleUserResponse> participants = new ArrayList<>();

        for(ScheduleUser scheduleUser : users){
            if(scheduleUser.getStatus().equals(InviteStatus.ACCEPTED)){
                participants.add(ScheduleUserResponse.fromEntity(scheduleUser));
            }
        }
        return participants;
    }

    public List<ScheduleUserResponse> getInvitationList (String email) {
        List<ScheduleUser> users = scheduleUserRepository.findByUserEmail(email);
        return users.stream()
                .map(ScheduleUserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void remove(Long scheduleUserId) {
        ScheduleUser scheduleUser = scheduleUserRepository
                .findById(scheduleUserId)
                .orElseThrow(() -> new RuntimeException("ScheduleUser not found"));

        scheduleUserRepository.delete(scheduleUser);
    }

    public List<ScheduleResponse> getInvitedSchedules (String email) {
        List<ScheduleUser> scheduleUsers = scheduleUserRepository.findByUserEmail(email);
        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        for(ScheduleUser scheduleUser : scheduleUsers){
            if(scheduleUser.getStatus().equals(InviteStatus.ACCEPTED)){
                scheduleResponses.add(ScheduleResponse.fromEntity(scheduleUser.getSchedule()));
            }
        }
        return scheduleResponses;
    }
}