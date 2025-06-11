package com.air.air.controller;

import com.air.air.model.User;
import com.air.air.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
//  React 프론트엔드 서버(포트 3000)에서 이 API에 접근할 수 있도록 CORS 허용
@RestController
@RequestMapping("/api/users")
//  "/api/users"로 시작하는 모든 요청 경로를 이 컨트롤러에서 처리
public class UserController {

    @Autowired
    private UserService userService;
    //  사용자 로직을 처리할 서비스 객체 (의존성 주입됨)

    /**
     *  회원가입 API
     * 요청: POST /api/users/register
     * 요청 본문: User 객체 (JSON 형태)
     * 응답: 회원가입 성공 여부 메시지 + 상태 코드
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("받은 데이터: " + user); // ✅ 프론트에서 받은 회원가입 데이터 로그 출력

        try {
            // 아이디 중복 체크
            if (userService.existsByUsername(user.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 아이디");
            }

            //  이메일 중복 체크
            if (userService.existsByEmail(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 이메일");
            }

            //  사용자 저장
            userService.saveUser(user);
            return ResponseEntity.ok(Map.of("message", "회원가입 성공!"));

        } catch (ResponseStatusException ex) {
            //  중복 오류 등 클라이언트 오류 반환
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        } catch (Exception ex) {
            //  기타 서버 내부 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }

    /**
     *  로그인 API
     * 요청: POST /api/users/login
     * 요청 본문: { "username": "아이디", "password": "비밀번호" }
     * 응답: 로그인 성공 시 사용자 정보, 실패 시 오류 메시지
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            //  사용자 인증 시도
            User user = userService.loginUser(username, password);

            //  로그인 성공 시 사용자 정보 응답
            return ResponseEntity.ok(Map.of(
                    "message", "로그인 성공!",
                    "name", user.getName(),
                    "username", user.getUsername(),
                    "email", user.getEmail(), //  사용자 이메일 응답 포함
                    "isLoggedIn", true //  로그인 상태 플래그
            ));

        } catch (ResponseStatusException ex) {
            //  로그인 실패 (ID 또는 비밀번호 오류 등)
            return ResponseEntity.status(ex.getStatusCode())
                    .body(Map.of("message", ex.getReason(), "isLoggedIn", false));
        } catch (Exception ex) {
            //  서버 오류 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류가 발생했습니다.", "isLoggedIn", false));
        }
    }

    /**
     *  회원 탈퇴 API
     * 요청: DELETE /api/users/{username}
     * 기능: 특정 사용자(username)를 삭제
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            //  사용자 삭제 시도
            userService.deleteByUsername(username);
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));

        } catch (ResponseStatusException ex) {
            //  사용자 없음 등의 클라이언트 오류
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        } catch (Exception ex) {
            //  서버 내부 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }

    // 🟩✅ [아이디(이메일) 찾기] 추가
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody Map<String, String> req) {
        String name = req.get("name");
        String phoneNumber = req.get("phoneNumber"); // [☑️] key 이름을 phone → phoneNumber로 수정!
        try {
            String email = userService.findEmailByNameAndPhone(name, phoneNumber);
            if (email != null) {
                return ResponseEntity.ok(Map.of("email", email));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "해당 정보로 가입된 계정이 없습니다."));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }

    // 🟩✅ [비밀번호 재설정 링크 발송] 추가
    @PostMapping("/reset-password-request")
    public ResponseEntity<?> requestResetPassword(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        try {
            boolean result = userService.sendResetPasswordEmail(email); // 🟩✅ 메일 전송 로직 호출
            if (result) {
                return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "해당 이메일로 가입된 계정이 없습니다."));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }

    // 🟩✅ [비밀번호 재설정] 추가
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {
        String token = req.get("token");
        String newPassword = req.get("newPassword");
        try {
            boolean result = userService.resetPassword(token, newPassword); // 🟩✅ 토큰 검증 및 비번 변경 로직 호출
            if (result) {
                return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "유효하지 않은 토큰입니다."));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }
}