package com.example.demo.repository;

import com.example.demo.domain.Schedule;
import com.example.demo.domain.ScheduleRole;
import com.example.demo.domain.ScheduleUser;
import com.example.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleUserRepository extends JpaRepository<ScheduleUser, Long> {
    boolean existsByScheduleAndUserAndRole(Schedule schedule, User owner, ScheduleRole scheduleRole);

    ScheduleUser findByScheduleIdAndUserEmail(Long scheduleId, String email);
}
