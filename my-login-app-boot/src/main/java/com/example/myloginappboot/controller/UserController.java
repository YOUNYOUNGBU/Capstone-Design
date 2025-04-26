package com.example.myloginappboot.controller;

import com.example.myloginappboot.entity.User;
import com.example.myloginappboot.service.UserService; // UserService import 필요
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("받은 데이터: " + user); // 요청 데이터 확인
        userService.saveUser(user); // 데이터 저장
        return ResponseEntity.ok(Map.of("message", "회원가입 성공!"));
    }

}
