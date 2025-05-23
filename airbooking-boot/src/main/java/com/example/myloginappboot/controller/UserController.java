package com.example.myloginappboot.controller;

import com.example.myloginappboot.entity.User;
import com.example.myloginappboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000") // ✅프론트 주소
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ 회원가입 처리
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("받은 데이터: " + user); // 요청 데이터 확인

        try {
            // 중복 아이디 체크
            if (userService.existsByUsername(user.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 아이디");
            }
            // 중복 이메일 체크
            if (userService.existsByEmail(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 이메일");
            }

            userService.saveUser(user);
            return ResponseEntity.ok(Map.of("message", "회원가입 성공!"));

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }

    // ✅ 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            User user = userService.loginUser(username, password);
            return ResponseEntity.ok(Map.of(
                    "message", "로그인 성공!",
                    "name", user.getName(),
                    "username", user.getUsername(),
                    "email", user.getEmail(), // ✅ 추가
                    "isLoggedIn", true // ✅ 로그인 여부 추가
            ));

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason(), "isLoggedIn", false));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류가 발생했습니다.", "isLoggedIn", false));
        }
    }

    // ✅ 회원 탈퇴 처리
    // UserController.java
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            userService.deleteByUsername(username);
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }


}
