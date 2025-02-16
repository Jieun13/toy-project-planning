package com.example.demo.repository;

import com.example.demo.domain.ScheduleUser;
import com.example.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleUserRepository extends JpaRepository<ScheduleUser, Long> {

    List<ScheduleUser> findScheduleUsersByScheduleId(Long scheduleId);

    List<ScheduleUser> findByUserEmail(String email);

    ScheduleUser findByUserEmailAndScheduleId(String email, Long scheduleId);

    void deleteByScheduleId(Long scheduleId);
}