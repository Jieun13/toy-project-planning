package com.example.demo.service;

import com.example.demo.domain.Schedule;
import com.example.demo.dto.ScheduleRequest;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findByDateRange(startDate, endDate);
    }

    public List<Schedule> findByUserId(Long userId) {
        User author = userService.findById(userId);
        return scheduleRepository.findAllByAuthor(author);
    }

    @Transactional
    public Schedule update(Long id, ScheduleRequest request, String author) {
        Schedule schedule = findById(id);
        return schedule.update(request.toEntity(author));
    }

    @Transactional
    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }
}
