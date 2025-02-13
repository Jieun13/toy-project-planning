package com.example.demo.repository;

import com.example.demo.domain.Schedule;
import com.example.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findById(long id);
    List<Schedule> findAllByAuthor(String author);

    // 특정 기간의 일정 조회 (JPQL 사용)
    @Query("SELECT s FROM Schedule s WHERE s.startTime >= :startDate AND s.endTime <= :endDate")
    List<Schedule> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM Schedule s WHERE s.startTime >= :startDate AND s.endTime <= :endDate")
    List<Schedule> findByDateRangeAndAuthor(LocalDateTime startDate, LocalDateTime endDate, User user);
}
