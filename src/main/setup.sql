-- ==========================================
-- 1. Tạo Database khớp với application.yml
-- ==========================================
CREATE DATABASE IF NOT EXISTS keep_up CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE keep_up;

-- ==========================================
-- LƯU Ý: 
-- Các bảng (account, role, user_profile...) sẽ được Spring Boot (Hibernate) 
-- tự động sinh ra nhờ dòng lệnh `ddl-auto: update` trong file application.yml.
-- Bạn hãy chạy Project một lần để Spring khởi tạo các bảng trước, 
-- sau đó mới bôi đen và chạy tiếp các lệnh INSERT dưới đây!
-- ==========================================

-- ==========================================
-- 2. Khởi tạo dữ liệu cốt lõi cho bảng role
-- ==========================================
INSERT INTO role (role_name) VALUES 
('ROLE_ADMIN'),
('ROLE_STUDENT');

-- ==========================================
-- 3. Khởi tạo dữ liệu Profile cho Admin
-- ==========================================
INSERT INTO user_profile (full_name, phone, avatar_url, gender, date_of_birth) 
VALUES ('System Administrator', '0987654321', NULL, 'MALE', '2000-01-01');

-- ==========================================
-- 4. Khởi tạo tài khoản Admin mặc định
-- Mật khẩu mặc định: 123456 
-- (Đã được băm sẵn bằng thuật toán BCrypt để Security có thể login ngay)
-- ==========================================
INSERT INTO account (email, password_hash, role_id, profile_id, provider_id, auth_provider, status, created_at, updated_at) 
VALUES (
    'admin@keepup.com', 
    '$2a$10$slYQmyNdGzTn7ZLBw1v.oO2uK4R3j2d/aCMyQ/.gC7Zf6HhHn.A5O', -- 123456
    (SELECT id FROM role WHERE role_name = 'ROLE_ADMIN' LIMIT 1), 
    (SELECT id FROM user_profile WHERE phone = '0987654321' LIMIT 1), 
    NULL, 
    NULL, 
    'ACTIVE', 
    NOW(), 
    NOW()
);
