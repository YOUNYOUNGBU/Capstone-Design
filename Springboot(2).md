## con
<pre>
  <code>
    package com.example.test.controller;

import java.util.List;
import com.example.test.service.TestService;
import com.example.test.entity.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public List getAllMembers() {
        return testService.getAllMembers();
    }

    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable Long id) {
        return testService.getMemberById(id);
    }
}

  </code>
</pre>

## mem
<pre>
  <code>
   package com.example.test.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "admin")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
}
  </code>
</pre>

## ser
<pre>
  <code>
    package com.example.test.service;

import com.example.test.entity.Member;
import com.example.test.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestService {
    private final MemberRepository memberRepository;

    public TestService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
    }
}


  </code>
</pre>

## rep
<pre>
  <code>
    package com.example.test.repository;

import com.example.test.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MemberRepository extends JpaRepository<Member", Long"> {
    // 추가적인 쿼리 메서드 정의 가능
}

  </code>
</pre>
