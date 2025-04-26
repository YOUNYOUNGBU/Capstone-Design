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