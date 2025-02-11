package com.example.demo.user.dto;

import com.example.demo.user.domain.User;
import lombok.Builder;

public class UserResponse {
    private Long id;
    private String name;
    private String email;

    @Builder
    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.email = user.getEmail();
    }
}
