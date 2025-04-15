## 1. UserService.java
<pre>
  <code>
    package com.example.myloginappboot.service;

import com.example.myloginappboot.entity.User;
import com.example.myloginappboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
  </code>
</pre>

## 2. UserRepository.java
<pre>
  <code>
    package com.example.myloginappboot.repository;

import com.example.myloginappboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
  </code>
</pre>

## 3. WebConfig.java
<pre>
  <code>
    package com.example.myloginappboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
  </code>
</pre>

## 4. User.java
<pre>
  <code>
    package com.example.myloginappboot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name; // React의 username 데이터와 매핑
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
}
  </code>
</pre>

## 5. UserController.java
<pre>
  <code>
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
  </code>
</pre>

