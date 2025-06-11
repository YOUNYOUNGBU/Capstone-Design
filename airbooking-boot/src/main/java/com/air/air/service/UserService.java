package com.air.air.service;

import com.air.air.model.User;
import com.air.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import org.springframework.mail.SimpleMailMessage; // ✅추가
import org.springframework.mail.javamail.JavaMailSender; // ✅추가

import java.util.UUID; // ✅추가
import java.util.concurrent.ConcurrentHashMap; // ✅추가
import java.util.Map; // ✅추가

@Service //  사용자 비즈니스 로직을 처리하는 서비스 계층 클래스
public class UserService {

    @Autowired
    private UserRepository userRepository; // 사용자 데이터 접근을 위한 JPA 레포지토리 주입

    @Autowired // ✅ 메일 발송기 주입
    private JavaMailSender mailSender; // ✅ 메일 발송


    // ✅ 토큰-이메일 매핑 임시저장
    private static final Map<String, String> resetTokens = new ConcurrentHashMap<>(); // ✅  토큰-이메일 매핑 임시저장

    /**
     *  회원가입 로직
     * - 아이디, 이메일 중복 체크
     * - 중복 시 400 오류 발생
     * - 중복이 없으면 userRepository.save(user) 호출로 DB 저장
     */
    public void saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 아이디");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 이메일");
        }

        userRepository.save(user);
    }

    /**
     * 로그인 인증 로직
     * - 사용자명으로 User 조회
     * - 비밀번호 일치 여부 확인
     * - 실패 시 401 UNAUTHORIZED 예외
     * - 성공 시 사용자 객체 반환
     */
    public User loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return user.get();
    }

    /**
     * 사용자 단건 조회 (username으로)
     * - 존재하지 않으면 404 오류 발생
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    /**
     * 아이디 중복 체크 (Boolean 반환)
     * - Controller에서 중복 여부 미리 확인할 때 사용
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 이메일 중복 체크
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 회원 탈퇴 처리
     * - 사용자 존재 여부 확인 후 삭제
     * - 존재하지 않으면 404 예외
     */
    public void deleteByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        userRepository.delete(user.get());
    }

    // ✅[아이디(이메일) 찾기]
    public String findEmailByNameAndPhone(String name, String phoneNumber) {
        Optional<User> user = userRepository.findByNameAndPhoneNumber(name, phoneNumber);
        return user.map(User::getEmail).orElse(null);
    }

    // ✅[비밀번호 재설정 메일 전송]
    public boolean sendResetPasswordEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, email);

        // --- 실제 메일 발송 코드 추가 시작 ---
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("비밀번호 재설정 안내");
            message.setText("아래 링크를 클릭해서 비밀번호를 재설정하세요:\n"
                    + "http://localhost:3000/reset-password?token=" + token);

            mailSender.send(message);

            System.out.println("비밀번호 재설정 메일 발송됨: " + email);
            System.out.println("비밀번호 재설정 링크: http://localhost:3000/reset-password?token=" + token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("메일 발송 실패: " + e.getMessage());
            return false;
        }
    }
    // ✅ [비밀번호 재설정] (토큰 검증 및 새 비밀번호 저장)
    public boolean resetPassword(String token, String newPassword) {
        String email = resetTokens.get(token);
        if (email == null) {
            return false; // 유효하지 않은 토큰
        }
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();
        user.setPassword(newPassword);
        userRepository.save(user);
        resetTokens.remove(token);
        return true;
    }
}