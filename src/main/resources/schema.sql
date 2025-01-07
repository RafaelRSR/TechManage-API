CREATE DATABASE IF NOT EXISTS db_techmanage;

USE db_techmanage;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL,
    user_type VARCHAR(20) NOT NULL,
    CONSTRAINT chk_user_type CHECK (user_type IN ('ADMIN', 'EDITOR', 'VIEWER'))
    );