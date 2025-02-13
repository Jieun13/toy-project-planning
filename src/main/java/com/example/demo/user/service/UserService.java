package com.example.demo.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.example.demo.user.dto.UserRequest;
import com.example.demo.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.user.domain.User;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long save(UserRequest userRequest) {
        return userRepository.save(User.builder()
                .username(userRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .build()).getId();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}