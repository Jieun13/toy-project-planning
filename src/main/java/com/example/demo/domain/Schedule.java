package com.example.demo.domain;

import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String author;

    @Builder
    public Schedule(String title, String description, LocalDateTime startTime, LocalDateTime endTime, String author) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.author = author;
    }

    public Schedule update(Schedule schedule) {
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.author = schedule.getAuthor();
        return this;
    }
}