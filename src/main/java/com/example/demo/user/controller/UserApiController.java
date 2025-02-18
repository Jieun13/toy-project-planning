package com.example.demo.user.controller;

import com.example.demo.user.domain.User;
import com.example.demo.user.config.CookieUtil;
import com.example.demo.user.dto.CreateAccessTokenRequest;
import com.example.demo.user.dto.CreateAccessTokenResponse;
import com.example.demo.user.config.TokenProvider;
import com.example.demo.user.service.TokenService;
import com.example.demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserApiController {

    private final TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request){
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

    @DeleteMapping("/logout")
    //쿠키 삭제
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.deleteCookie(request, response, "jwt_token");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
