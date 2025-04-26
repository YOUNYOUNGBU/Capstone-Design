CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY, -- 기본키와 자동 증가 설정
    name VARCHAR(255) NOT NULL,            -- 이름 필드
    email VARCHAR(255) NOT NULL UNIQUE,    -- 이메일 필드 (중복 금지)
    password VARCHAR(255) NOT NULL,        -- 비밀번호 필드
    phone_number VARCHAR(15) NOT NULL            -- 전화번호 필드
);
