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
    private final TokenProvider tokenProvider;
    private final UserService userService;


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

    @GetMapping("/profile")
    public ResponseEntity<User> profile(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // "Bearer {token}"에서 실제 토큰만 추출
        String token = authorizationHeader.substring(7);

        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long userId = tokenProvider.getUserId(token);
            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // 잘못된 토큰 처리
        }
    }
}
