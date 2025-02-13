//package com.example.demo;
//
//import com.example.demo.repository.ScheduleUserRepository;
//import com.example.demo.user.domain.User;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class ScheduleEventListner {
//    private final ScheduleUserRepository scheduleUserRepository;
//
//    public ScheduleEventListner(ScheduleUserRepository scheduleUserRepository) {
//        this.scheduleUserRepository = scheduleUserRepository;
//    }
//
//    @EventListener
//    public void handleScheduleUpdatedEvent(ScheduleUpdatedEvent event){
//        List<User> invitees = scheduleUserRepository.findAcceptedUsersBySchedule(event.getSchedule());
//        for(User invitee : invitees){
//            notificationService.sendNotification(invitee, "일정이 변경되었습니다." + event.getSchedule().getTitle());
//        }
//    }
//}
