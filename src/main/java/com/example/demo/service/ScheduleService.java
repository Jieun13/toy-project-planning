package com.example.demo.service;

import com.example.demo.domain.Schedule;
import com.example.demo.dto.ScheduleRequest;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    @Transactional
    public Schedule save(ScheduleRequest request, String author) {
        Schedule newSchedule = request.toEntity(author);
        return scheduleRepository.save(newSchedule);
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public List<Schedule> findAllByEmail(String email) {
        List<Schedule> schedules = scheduleRepository.findAllByAuthor(email);

        if (schedules.isEmpty()) {
            System.out.println("No schedules found for author: " + email);
            return Collections.emptyList();
        }
        return schedules;
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByDateRangeAndAuthor(LocalDateTime startDate, LocalDateTime endDate, String email) {
        User user = userService.findByEmail(email);
        return scheduleRepository.findByDateRangeAndAuthor(startDate, endDate, user);
    }

    @Transactional
    public Schedule update(Long id, ScheduleRequest request, String author) {
        Schedule schedule = findById(id);
        validateAuthor(schedule, author); // 🔹 작성자 검증 로직을 메서드로 분리
        return schedule.update(request.toEntity(author));
    }

    @Transactional
    public void delete(Long id, String author) {
        Schedule schedule = findById(id);
        validateAuthor(schedule, author); // 🔹 작성자 검증
        scheduleRepository.delete(schedule); // 🔹 deleteById → delete(schedule) 변경
    }

    // 🔹 중복되는 작성자 검증 로직을 별도 메서드로 분리
    private void validateAuthor(Schedule schedule, String author) {
        if (!schedule.getAuthor().equals(author)) {
            throw new AccessDeniedException("수정/삭제 권한이 없습니다.");
        }
    }
}