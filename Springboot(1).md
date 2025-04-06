# 스프링 부트 연습

## Member.java
<pre>
  <code>
    package Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

}

  </code>
</pre>

## TestController.java
<pre>
  <code>
    package Controller;

import Entity.Member;
import Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestController {
    @Autowired
    TestService testService;

    @GetMapping("/test")
    public List<Member> getAllMembers(){
        List<Member> members = testService.getAllMembers();
        return members;
    }
}

  </code>
</pre>

## TestService.java
<pre>
  <code>
    package Service;

import Entity.Member;
import Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

    public class TestService{
        @Autowired
        MemberRepository memberRepository;

        public List<Member> getAllMembers(){
            return memberRepository.findAll();
        }
    }


  </code>
</pre>

## MemberRepository.java
<pre>
  <code>
    package Repository;

import Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


}

  </code>
</pre>

## 결과 화면

![image](https://github.com/user-attachments/assets/d83dadaa-0984-4a4d-ad9f-ab4426b6f449)


