con
<pre>
  <code>
    import java.util.List;
import Service.TestService;
import Entity.Member;

@RestController
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return testService.getAllMembers();
    }

    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable Long id) {
        return testService.getMemberById(id);
    }
}

  </code>
</pre>

mem
<pre>
  <code>
    package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "admin")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id", nullable = false)
    private String adminId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
}

  </code>
</pre>

ser
<pre>
  <code>
    package Service;

import Entity.Member;
import Repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestService {
    private final MemberRepository memberRepository;

    public TestService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
    }
}

  </code>
</pre>
