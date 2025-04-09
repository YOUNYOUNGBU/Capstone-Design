오늘은 리액트로 로그인 화면, 회원가입 화면을 만든후
스프링 부트를 이용해서, 회원가입 시 사용자 정보를 데이터베이스에 저장하는 것을 해봤다.

# 우선 리액트 코드이다.
## Login.jsx
&lt;pre&gt;&lt;code&gt;
    import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css'; // 스타일 파일 가져오기

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log('Username:', username);
        console.log('Password:', password);
    };

    const goToRegister = () => {
        navigate('/register'); // 회원가입 페이지로 이동
    };

    return (
  
        <div className="login-container">
            <h2 className="login-title">로그인</h2>
            <form onSubmit={handleSubmit} className="login-form">
                <div className="form-group">
                    <label htmlFor="username">사용자 이름</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="사용자 이름을 입력하세요"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">비밀번호</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="비밀번호를 입력하세요"
                        required
                    />
                </div>
                <button type="submit" className="login-button">로그인</button>
            </form>
            <button onClick={goToRegister} className="register-link">회원가입</button>
        </div>
    );
}

export default Login;
 &lt;/pre&gt;&lt;/code&gt;

## 결과 화면
![image](https://github.com/user-attachments/assets/653e2fcd-d484-495d-b994-a40d81ec75c8)

## Register.jsx
<pre>
  <code>
    

    import React, { useState } from 'react';
import './Register.css';

function Register() {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [userId, setUserId] = useState(''); // user_Id 추가
    const [phoneNumber, setPhoneNumber] = useState(''); // phone_number 추가
    const [message, setMessage] = useState(''); // 성공/실패 메시지 상태 추가

    const handleRegister = async () => {
        const user = {
            userId: userId,
            email: email,
            name: username, // "username"을 "name"으로 매핑
            phoneNumber: phoneNumber,
            password: password,
        };

        try {
            const response = await fetch('http://localhost:8080/api/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user),
            });

            if (response.ok) {
                const data = await response.json();
                console.log('회원가입 성공:', data);
                setMessage('회원가입 성공!'); // 성공 메시지 설정
            } else {
                console.error('회원가입 실패');
                setMessage('회원가입 실패!'); // 실패 메시지 설정
            }
        } catch (error) {
            console.error('에러 발생:', error);
            setMessage('에러 발생! 다시 시도해주세요.'); // 에러 메시지 설정
        }
    };

    return (
        <div className="register-container">
            <h2 className="register-title">회원가입</h2>
            {message && <p className="register-message">{message}</p>} {/* 메시지 표시 */}
            <form className="register-form">
                <div className="form-group">
                    <label htmlFor="userId">사용자 ID</label>
                    <input
                        type="text"
                        id="userId"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        placeholder="사용자 ID를 입력하세요"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">이메일</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="이메일을 입력하세요"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="username">사용자 이름</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="사용자 이름을 입력하세요"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="phoneNumber">전화번호</label>
                    <input
                        type="tel"
                        id="phoneNumber"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        placeholder="전화번호를 입력하세요"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">비밀번호</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="비밀번호를 입력하세요"
                        required
                    />
                </div>
                <button type="button" onClick={handleRegister} className="register-button">
                    회원가입
                </button>
            </form>
        </div>
    );
}
export default Register;

  </code>
</pre>

## 결과 화면
![image](https://github.com/user-attachments/assets/db1831f7-c06f-422d-a231-b3f30f8a727b)







