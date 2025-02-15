package com.example.demo.repository;

import com.example.demo.domain.Schedule;
import com.example.demo.domain.ScheduleRole;
import com.example.demo.domain.ScheduleUser;
import com.example.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleUserRepository extends JpaRepository<ScheduleUser, Long> {
    boolean existsByScheduleAndUserAndRole(Schedule schedule, User owner, ScheduleRole scheduleRole);

    ScheduleUser findByScheduleIdAndUserEmail(Long scheduleId, String email);
    List<ScheduleUser> findScheduleUsersByScheduleId(Long scheduleId);

    List<ScheduleUser> findByUserEmail(String email);

    ScheduleUser findByUserEmailAndScheduleId(String email, Long scheduleId);

    Optional<ScheduleUser> findByScheduleAndUser_Email(Schedule schedule, String email);
    void deleteByScheduleId(Long scheduleId);
}