CREATE TABLE `user` (
    `id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT "id",
    `email` VARCHAR(64) NOT NULL COMMENT "이메일",
    `password` VARCHAR(128) NOT NULL COMMENT "패스워드",
    `nickname` VARCHAR(32) NOT NULL COMMENT "사용자 이름",
    `role` VARCHAR(8) NOT NULL COMMENT "권한",
    `refresh_token` VARCHAR(256) NULL COMMENT "리프레시 토큰",
    `last_login_at` TIMESTAMP NULL COMMENT "마지막 로그인 시간",
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "생성 일자",
    `updated_at` TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT "수정 일자",
    UNIQUE KEY `uk_user_email` (`email`)
);

CREATE TABLE `partner` (
    `id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT "id",
    `name` VARCHAR(64) NOT NULL COMMENT "제휴사명",
    `is_opened` BIT NOT NULL COMMENT "오픈여부",
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "생성 일자",
    `updated_at` TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT "수정 일자",
    UNIQUE KEY `uk_partner_name` (`name`)
);

CREATE TABLE `product` (
    `id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT "id",
    `name` VARCHAR(64) NOT NULL COMMENT "제휴사명",
    `is_opened` BIT NOT NULL COMMENT "오픈여부",
    `partner_id` BIGINT NOT NULL COMMENT "파트너 id",
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "생성 일자",
    `updated_at` TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT "수정 일자",
    UNIQUE KEY `uk_product_name` (`name`),
    INDEX `idx_product_partner_id` (`partner_id`),
    INDEX `idx_product_partner_id_is_opened` (`partner_id`, `is_opened`)
);

CREATE TABLE `loan_comparison` (
    `id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT "id",
    `user_id` BIGINT NOT NULL COMMENT "유저 id",
    `partner_id` BIGINT NOT NULL COMMENT "파트너 id",
    `product_id` BIGINT NOT NULL COMMENT "상품 id",
    `status` ENUM("PENDING", "ACCEPTED", "REJECTED") NOT NULL COMMENT "상태",
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "생성 일자",
    `updated_at` TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT "수정 일자"
);
