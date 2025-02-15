package com.example.demo.domain;

import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="schedule_user_table")
@Getter
@NoArgsConstructor
public class ScheduleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scheduleName;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Enumerated(EnumType.STRING)
    private ScheduleRole role;      // OWNER, EDITOR, VIEWER

    @Setter
    @Enumerated(EnumType.STRING)
    private InviteStatus status;    // PENDING(대기), ACCEPTED, DECLINED

    @Builder
    public ScheduleUser (String scheduleName, Schedule schedule, User user, ScheduleRole role, InviteStatus status) {
        this.scheduleName = scheduleName;
        this.schedule = schedule;
        this.user = user;
        this.role = role;
        this.status = status;
    }
}