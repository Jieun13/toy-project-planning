package com.example.demo.service;

import com.example.demo.domain.InviteStatus;
import com.example.demo.domain.Schedule;
import com.example.demo.domain.ScheduleRole;
import com.example.demo.domain.ScheduleUser;
import com.example.demo.dto.InviteRequest;
import com.example.demo.dto.ScheduleRequest;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.repository.ScheduleUserRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleUserService {
    private final ScheduleUserRepository scheduleUserRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleUser inviteUser(Long scheduleId, InviteRequest request, String email) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()->new RuntimeException("일정을 찾을 수 없습니다."));
        User owner = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("사용자를 찾을 수 없습니다."));
        User invitee = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("초대할 사용자를 찾을 수 없습니다."));

        if(!scheduleUserRepository.existsByScheduleAndUserAndRole(schedule, owner, ScheduleRole.OWNER)){
            throw new RuntimeException("일정 소유자만 초대할 수 있습니다.");
        }

        ScheduleUser scheduleUser = ScheduleUser.builder()
                .schedule(schedule)
                .user(invitee)
                .role(ScheduleRole.OWNER)
                .status(InviteStatus.PENDING)
                .build();

        return scheduleUserRepository.save(scheduleUser);
    }

    public void respondToInvite(Long scheduleId, String email, boolean accept) {
        ScheduleUser scheduleUser = scheduleUserRepository.findByScheduleIdAndUserEmail(scheduleId, email);
        if (accept){
            scheduleUser.setStatus(InviteStatus.ACCEPTED);
        }
        else {
            scheduleUser.setStatus(InviteStatus.DECLINED);
        }
        scheduleUserRepository.save(scheduleUser);
    }
}
