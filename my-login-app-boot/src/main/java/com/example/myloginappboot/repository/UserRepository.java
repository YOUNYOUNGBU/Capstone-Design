package com.example.myloginappboot.repository;

import com.example.myloginappboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
